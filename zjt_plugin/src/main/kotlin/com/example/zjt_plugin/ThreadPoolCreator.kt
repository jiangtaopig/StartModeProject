package com.example.zjt_plugin

import org.objectweb.asm.Opcodes

/**
 * @Author ZhuJiangTao
 * @Since 2021/7/31
 */
object ThreadPoolCreator {

    val poolList = mutableListOf<PoolEntity>()
    const val EXECUTORS_OWNER = "java/util/concurrent/Executors"

    init {
        val fix = PoolEntity(
            Opcodes.INVOKESTATIC,
            "java/util/concurrent/Executors",
            "newFixedThreadPool",
            "(I)Ljava/util/concurrent/ExecutorService;",
            "getThreadPool"
        )
        poolList.add(fix)
        var single = PoolEntity(
            Opcodes.INVOKESTATIC,
            "java/util/concurrent/Executors",
            "newSingleThreadExecutor",
            "()Ljava/util/concurrent/ExecutorService;",
            "getThreadPool"
        )
        poolList.add(single)
        single = PoolEntity(
            Opcodes.INVOKESTATIC,
            "java/util/concurrent/Executors",
            "newSingleThreadExecutor",
            "(Ljava/util/concurrent/ThreadFactory;)Ljava/util/concurrent/ExecutorService;",
            "getThreadPool"
        )
        poolList.add(single)
        val work = PoolEntity(
            Opcodes.INVOKESTATIC,
            "java/util/concurrent/Executors",
            "newWorkStealingPool",
            "(I)Ljava/util/concurrent/ExecutorService;"
        )
        poolList.add(work)
        val cache = PoolEntity(
            Opcodes.INVOKESTATIC,
            "java/util/concurrent/Executors",
            "newCachedThreadPool",
            "(I)Ljava/util/concurrent/ExecutorService;",
            "getThreadPool"
        )
        poolList.add(cache)

        val schedule = PoolEntity(
            Opcodes.INVOKESTATIC,
            "java/util/concurrent/Executors",
            "newSingleThreadScheduledExecutor",
            "()Ljava/util/concurrent/ExecutorService;",
            "getThreadPool"
        )
        poolList.add(schedule)
    }
}