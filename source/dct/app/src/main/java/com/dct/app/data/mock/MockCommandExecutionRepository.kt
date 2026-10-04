package com.dct.app.data.mock

import com.dct.app.domain.model.CommandStatus
import com.dct.app.domain.model.CommandTask
import com.dct.app.domain.repository.CommandExecutionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

/** 模拟实现：不会执行任何真实命令，所有输出均带 [模拟] 前缀。 */
class MockCommandExecutionRepository : CommandExecutionRepository {
    private val tasks = MutableStateFlow(
        listOf(
            CommandTask(
                "t1", "git status", CommandStatus.SUCCESS, 0, 120,
                listOf("[模拟] On branch main", "[模拟] nothing to commit, working tree clean"),
            ),
            CommandTask(
                "t2", "npm test", CommandStatus.FAILED, 1, 3400,
                listOf("[模拟] FAIL src/App.test.tsx", "[模拟] 1 test failed"),
            ),
        )
    )

    override fun observeTasks(): Flow<List<CommandTask>> = tasks.asStateFlow()

    override suspend fun submit(command: String) {
        val cmd = command.trim()
        if (cmd.isEmpty()) return
        val id = UUID.randomUUID().toString()
        tasks.update {
            listOf(
                CommandTask(id, cmd, CommandStatus.RUNNING, null, null, listOf("[模拟] 命令已排队，并未在 Termux 中执行")),
            ) + it
        }
        delay(800)
        // 演示规则：命令中包含 "fail" 时模拟失败，其余模拟成功。
        val failed = cmd.contains("fail", ignoreCase = true)
        tasks.update { list ->
            list.map {
                if (it.id != id) it else it.copy(
                    status = if (failed) CommandStatus.FAILED else CommandStatus.SUCCESS,
                    exitCode = if (failed) 1 else 0,
                    durationMs = 800,
                    output = it.output + if (failed) "[模拟] 模拟失败（退出码 1）" else "[模拟] 模拟完成（退出码 0）",
                )
            }
        }
    }
}
