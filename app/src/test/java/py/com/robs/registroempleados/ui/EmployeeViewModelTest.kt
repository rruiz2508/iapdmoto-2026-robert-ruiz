package py.com.robs.registroempleados.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EmployeeViewModelTest {

    @Test
    fun `addEmployee inserts a normalized employee`() {
        val viewModel = EmployeeViewModel()

        viewModel.addEmployee(
            fullName = "  Robert Ruiz  ",
            position = "  Arquitecto  ",
            department = "  Tecnología  ",
            salary = 12_500_000.0,
            hireDate = "17/07/2026",
        )

        assertEquals(1, viewModel.employees.size)
        assertEquals("Robert Ruiz", viewModel.employees.first().fullName)
        assertEquals("Arquitecto", viewModel.employees.first().position)
        assertEquals("Tecnología", viewModel.employees.first().department)
    }

    @Test
    fun `deleteEmployee removes only the selected employee`() {
        val viewModel = EmployeeViewModel()
        viewModel.addEmployee("Ana", "Analista", "TIC", 7_000_000.0, "01/02/2025")
        viewModel.addEmployee("Luis", "Contador", "Finanzas", 6_500_000.0, "03/04/2025")
        val employeeToDelete = viewModel.employees.first()

        viewModel.deleteEmployee(employeeToDelete)

        assertEquals(1, viewModel.employees.size)
        assertTrue(viewModel.employees.none { it.id == employeeToDelete.id })
    }
}

