package com.dct.app.data.mock

import com.dct.app.domain.model.CommitInfo
import com.dct.app.domain.model.GitHubRepo
import com.dct.app.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockGitHubRepository : GitHubRepository {
    private val repos = MutableStateFlow(
        listOf(
            GitHubRepo("r1", "demo-user/tovikeli-web", "Mock 仓库：兴趣社区前端", "p1"),
            GitHubRepo("r2", "demo-user/aimx-core", "Mock 仓库：AI 视觉内容本地化", "p2"),
            GitHubRepo("r3", "demo-user/dct", "Mock 仓库：Developer Convenience Tools", null),
        )
    )

    override fun observeRepos(): Flow<List<GitHubRepo>> = repos.asStateFlow()

    override suspend fun getCommits(repoId: String): List<CommitInfo> = listOf(
        CommitInfo("a1b2c3d", "feat: 添加登录页占位", "demo-user", "2 小时前"),
        CommitInfo("e4f5a6b", "fix: 修复列表滚动抖动", "demo-user", "昨天"),
        CommitInfo("c7d8e9f", "chore: 升级依赖", "demo-user", "3 天前"),
    )

    override suspend fun getWorkingDiff(projectId: String): String = """
        --- a/src/App.tsx
        +++ b/src/App.tsx
        @@ -10,4 +10,5 @@
         function App() {
        -  return <Home />
        +  return <Home title="Mock" />
         }
    """.trimIndent()
}
