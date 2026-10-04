package com.dct.app.di

import androidx.compose.runtime.staticCompositionLocalOf
import com.dct.app.data.mock.MockAiAssistantRepository
import com.dct.app.data.mock.MockCommandExecutionRepository
import com.dct.app.data.mock.MockGitHubRepository
import com.dct.app.data.mock.MockProjectRepository
import com.dct.app.domain.repository.AiAssistantRepository
import com.dct.app.domain.repository.CommandExecutionRepository
import com.dct.app.domain.repository.GitHubRepository
import com.dct.app.domain.repository.ProjectRepository

/** 手动依赖注入容器。后续替换真实实现时只需修改这里。 */
class AppContainer {
    val projectRepository: ProjectRepository = MockProjectRepository()
    val gitHubRepository: GitHubRepository = MockGitHubRepository()
    val aiAssistantRepository: AiAssistantRepository = MockAiAssistantRepository()
    val commandRepository: CommandExecutionRepository = MockCommandExecutionRepository()
}

val LocalAppContainer = staticCompositionLocalOf<AppContainer> {
    error("AppContainer 未提供")
}
