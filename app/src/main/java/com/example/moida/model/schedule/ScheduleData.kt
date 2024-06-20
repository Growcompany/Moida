package com.example.moida.model.schedule

data class UserTime(
    val scheduleId: String,
    val userName: String,
    val time1: String,
    val time2: String,
    val time3: String,
    val time4: String,
    val time5: String,
    val time6: String,
    val time7: String,
) {
    constructor() : this("-1", "", "", "", "", "", "", "", "")
}

data class UserTime2( //scheduleid는 이미 같고 시간 계산용
    val userName: String,
    val time1: String,
    val time2: String,
    val time3: String,
    val time4: String,
    val time5: String,
    val time6: String,
    val time7: String,
){
    constructor() : this("" ,"", "", "", "", "", "", "")
}

data class ScheduleData(
    val scheduleId: Int,
    val scheduleStartDate: String = "",
    val scheduleTime: String = "",
    val scheduleName: String = "",
    val category: String = "",
//    var memberTimes: Map<String, String>?
) {
    constructor() : this(-1, "", "", "", "") // default 생성자
}

data class FixedScheduleData( //확정된 약속
    var scheduleId: Int,
    var scheduleName: String,
    var scheduleDate: String,
    var scheduleTime: String,
    var category: String
) {
    constructor() : this(-1,"","","","")
}