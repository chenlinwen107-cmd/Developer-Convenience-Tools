package com.dct.app.data.mock

import com.dct.app.domain.model.GitStatus
import com.dct.app.domain.model.Project
import com.dct.app.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockProjectRepository : ProjectRepository {
    private val projects = MutableStateFlow(
        listOf(
            Project("p1", "tovikeli-web", "~/projects/tovikeli-web", "main", GitStatus.MODIFIED, "10 分钟前"),
            Project("p2", "aimx-core", "~/projects/aimx-core", "feature/ocr", GitStatus.AHEAD, "昨天"),
            Project("p3", "dct", "~/projects/dct", "main", GitStatus.CLEAN, "3 天前"),
        )
    )

    override fun observeProjects(): Flow<List<Project>> = projects.asStateFlow()

    override suspend fun getProject(id: String): Project? = projects.value.firstOrNull { it.id == id }
}
