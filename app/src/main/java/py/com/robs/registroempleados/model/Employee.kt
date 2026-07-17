package py.com.robs.registroempleados.model

data class Employee(
    val id: Long,
    val fullName: String,
    val position: String,
    val department: String,
    val salary: Double,
    val hireDate: String,
)

