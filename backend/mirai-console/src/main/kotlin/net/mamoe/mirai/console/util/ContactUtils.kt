/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:Suppress("unused")

package net.mamoe.mirai.console.util

import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Member

/**
 * 为简化操作提供的一些工具
 */
@ConsoleExperimentalAPI
public object ContactUtils {
    /**
     * 获取一个 [Bot] 的好友, 群, 或群员.
     *
     * 访问顺序为 [Bot.friends] -> [Bot.groups] -> 各群的群员
     *
     * **注意**: 若目标用户存在于多个群, 可能返回任意一个群的 [Member].
     *
     * @see includeMembers 为 `false` 时不查找群成员
     */
    @JvmOverloads
    @JvmStatic
    @ConsoleExperimentalAPI
    public fun Bot.getContact(id: Long, includeMembers: Boolean = false): Contact {
        return getContactOrNull(id, includeMembers)
            ?: throw NoSuchElementException("Contact $id not found for bot ${this.id}")
    }

    /**
     * 获取一个 [Bot] 的好友, 群, 或群员.
     *
     * 访问顺序为 [Bot.friends] -> [Bot.groups] -> 各群的群员
     */
    @JvmOverloads
    @JvmStatic
    @ConsoleExperimentalAPI
    public fun Bot.getContactOrNull(id: Long, includeMembers: Boolean = false): Contact? {
        return getFriendOrGroupOrNull(id) ?: kotlin.run {
            if (includeMembers) {
                groups.asSequence().flatMap { it.members.asSequence() }.firstOrNull { it.id == id }
            } else null
        }
    }


    /**
     * 获取一个 [Bot] 的好友, 或群对象.
     *
     * 访问顺序为 [Bot.friends] -> [Bot.groups]
     */
    @JvmStatic
    @ConsoleExperimentalAPI
    public fun Bot.getFriendOrGroup(id: Long): Contact {
        return getFriendOrGroupOrNull(id)
            ?: throw NoSuchElementException("Friend or Group $id not found for bot ${this.id}")
    }

    /**
     * 获取一个 [Bot] 的好友, 或群对象.
     *
     * 访问顺序为 [Bot.friends] -> [Bot.groups]
     */
    @JvmStatic
    @ConsoleExperimentalAPI
    public fun Bot.getFriendOrGroupOrNull(id: Long): Contact? {
        return this.friends.getOrNull(id) ?: this.groups.getOrNull(id)
    }

}