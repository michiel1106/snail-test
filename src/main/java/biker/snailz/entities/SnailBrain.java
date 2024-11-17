package biker.snailz.entities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.LookAroundTask;
import net.minecraft.entity.ai.brain.task.MoveToTargetTask;
import net.minecraft.entity.ai.brain.task.StayAboveWaterTask;
import net.minecraft.entity.ai.brain.task.TickCooldownTask;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class SnailBrain {
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULE_TYPES;
    private static final float WALKING_SPEED;
    protected static final ImmutableList<SensorType<? extends Sensor<? super SnailEntity>>> SENSOR_TYPES;

    public SnailBrain() {}

    private static void addCoreActivities(Brain<SnailEntity> brain) {
        brain.setTaskList(Activity.CORE, 0, ImmutableList.of(
                new StayAboveWaterTask<>(.8f),
                new LookAroundTask(UniformIntProvider.create(40,50),90,0,0)));

    }


    protected static Brain<?> create(SnailEntity snail, Brain<SnailEntity> brain) {
        addCoreActivities(brain);
        //addIdleActivities(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }


    public static Brain.Profile<SnailEntity> createBrainProfile() {
        return Brain.createProfile(MEMORY_MODULE_TYPES, SENSOR_TYPES);
    }

    protected static void tickActivities(SnailEntity entity) {

        Brain<SnailEntity> brain = entity.getBrain();

        Activity activity = brain.getFirstPossibleNonCoreActivity().orElse(null);



        // if admiring


        brain.resetPossibleActivities(ImmutableList.of(Activity.IDLE));
        Activity activity2 = brain.getFirstPossibleNonCoreActivity().orElse(null);



    }









    static {

    WALKING_SPEED = SnailEntity.WALKING_SPEED;

    SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS,
            SensorType.NEAREST_ITEMS, SensorType.HURT_BY);

    MEMORY_MODULE_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH,
            MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.NEAREST_VISIBLE_PLAYER);
}

}
