package cn.quickits.polaris.data

import cn.quickits.polaris.util.DirUtils
import java.util.*

class PageStack {

    private val pages: Stack<Page> = Stack()

    init {
        pages.push(Page(DirUtils.rootDirPath(), 0))
    }

    fun push(dir: String) {
        pages.push(Page(dir, 0))
    }

    fun pop(): Page? {
        return if (pages.isEmpty()) null
        else pages.pop()
    }

    fun peek(): Page? {
        return if (pages.isEmpty()) null
        else pages.peek()
    }

}