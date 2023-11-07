package com.privateinternetaccess.regions.model

/*
 *  Copyright (c) 2023 Private Internet Access, Inc.
 *
 *  This file is part of the Private Internet Access Mobile Client.
 *
 *  The Private Internet Access Mobile Client is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  The Private Internet Access Mobile Client is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with the Private
 *  Internet Access Mobile Client.  If not, see <https://www.gnu.org/licenses/>.
 */

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
public data class ShadowsocksRegionsResponse(
    @SerialName("region")
    val region: String,
    @SerialName("host")
    val host: String,
    @SerialName("port")
    val port: Int,
    @SerialName("key")
    val key: String,
    @SerialName("cipher")
    val cipher: String
)
