package com.mifish.bloomfilter.center.strategy;

import com.mifish.bloomfilter.center.model.BloomFilterTask;
import com.mifish.bloomfilter.center.model.BloomFilterTaskPlan;
import com.mifish.bloomfilter.center.model.TaskMeta;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-19 15:34
 */
public class AbstractTaskOptimizeStrategy implements TaskOptimizeStrategy {

    /**
     * optimize
     *
     * @param bftask
     * @param taskMetas
     * @return
     */
    @Override
    public BloomFilterTaskPlan optimize(BloomFilterTask bftask, List<TaskMeta> taskMetas) {
        if (bftask == null || taskMetas == null) {
            throw new IllegalArgumentException("bftask or taskMetas cannot be null");
        }
        bftask.addAttribute("taskMetas", taskMetas);
        BloomFilterTaskPlan execPlan = new BloomFilterTaskPlan(bftask);
        Optional<List<BloomFilterTask>> op = doOptimize(bftask, taskMetas);
        if (op.isPresent()) {
            for (BloomFilterTask subTask : op.get()) {
                execPlan.addPlanTask(subTask);
            }
        }
        return execPlan;
    }

    /**
     * doOptimize
     *
     * @param bftask
     * @param taskMetas
     * @return
     */
    protected Optional<List<BloomFilterTask>> doOptimize(BloomFilterTask bftask, List<TaskMeta> taskMetas) {
        if (bftask == null || taskMetas == null) {
            return Optional.empty();
        }
        List<BloomFilterTask> optimizeTasks = new ArrayList<>(taskMetas.size() + 1);
        optimizeTasks.add(bftask);
        boolean isStart = false;
        BloomFilterTask preTask, nextTask;
        for (TaskMeta taskMeta : taskMetas) {
            if (StringUtils.equals(taskMeta.getTaskName(), bftask.getBloomFilterName())) {
                isStart = true;
                continue;
            }
            if (isStart) {
                BloomFilterTask.BloomFilterTaskType subTaskType = bftask.isBuildTask() ? BloomFilterTask
                        .BloomFilterTaskType.SUB_BUILD_TASK : BloomFilterTask.BloomFilterTaskType.SUB_LOAD_TASK;
                BloomFilterTask subTask = BloomFilterTask.buildSubTask(bftask.getGroup(), taskMeta.getTaskName(),
                        taskMeta.getTaskOrder(), subTaskType);
                subTask.addAttribute("taskMetas", taskMetas);
                optimizeTasks.add(subTask);
            } else {
                
            }
        }
        return Optional.of(optimizeTasks);
    }
}
