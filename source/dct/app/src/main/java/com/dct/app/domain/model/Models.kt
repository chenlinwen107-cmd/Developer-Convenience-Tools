package com.dct.app.domain.model

enum class GitStatus { CLEAN, MODIFIED, AHEAD }

data class Project(
    val id: String,
    val name: String,
    val path: String,
    val branch: String,
    val gitStatus: GitStatus,
    val lastOpened: String,
)

enum class CommandStatus { RUNNING, SUCCESS, FAILED }

/** 命令任务。第一阶段所有任务均为模拟，[isSimulated] 恒为 true。 */
data class CommandTask(
    val id: String,
    val command: String,
    val status: CommandStatus,
    val exitCode: Int?,
    val durationMs: Long?,
    val output: List<String>,
    val isSimulated: Boolean = true,
)

enum class MessageRole { USER, ASSISTANT }

data class AiMessage(
    val id: String,
    val role: MessageRole,
    val text: String,
)

enum class ChangeState { SUGGESTION, PENDING_REVIEW, APPLIED, REJECTED }

data class ChangeProposal(
    val id: String,
    val title: String,
    val summary: String,
    val filePath: String,
    val diff: String,
    val state: ChangeState,
)

data class GitHubRepo(
    val id: String,
    val fullName: String,
    val description: String,
    val linkedProjectId: String?,
)

data class CommitInfo(
    val hash: String,
    val message: String,
    val author: String,
    val time: String,
)
