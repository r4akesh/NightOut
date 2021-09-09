package com.nightout.vendor.services

import java.io.Serializable

data class BaseModell<T>(var status_code: Int, var message: String?, var data: T?) : Serializable