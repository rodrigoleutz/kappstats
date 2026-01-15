package com.kappstats.util

import com.kappstats.constants.ORGANISATION
import com.kappstats.constants.PACKAGE_NAME
import com.kappstats.constants.VERSION
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import net.harawata.appdirs.AppDirsFactory

fun getLocalAppPath(): String {
    val filesDir =
        AppDirsFactory.getInstance().getUserDataDir(PACKAGE_NAME, VERSION, ORGANISATION)
    with(SystemFileSystem) { if (!exists(Path(filesDir))) createDirectories(Path(filesDir)) }
    return filesDir
}