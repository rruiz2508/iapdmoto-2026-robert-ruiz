package py.com.robs.registroempleados.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import py.com.robs.registroempleados.model.Employee
import py.com.robs.registroempleados.ui.theme.RegistroEmpleadosTheme
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeScreen(
    employees: List<Employee>,
    darkTheme: Boolean,
    onThemeChange: () -> Unit,
    onAddEmployee: (String, String, String, Double, String) -> Unit,
    onDeleteEmployee: (Employee) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Equipo",
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                actions = {
                    IconButton(onClick = onThemeChange) {
                        Icon(
                            imageVector = if (darkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (darkTheme) "Activar tema claro" else "Activar tema oscuro",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                WelcomeCard(employeeCount = employees.size)
            }

            item {
                EmployeeForm(onAddEmployee = onAddEmployee)
            }

            item {
                EmployeeListHeader(employeeCount = employees.size)
            }

            if (employees.isEmpty()) {
                item { EmptyEmployeeList() }
            } else {
                items(
                    items = employees,
                    key = Employee::id,
                ) { employee ->
                    EmployeeCard(
                        employee = employee,
                        onDelete = {
                            onDeleteEmployee(employee)
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "${employee.fullName} fue eliminado",
                                )
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun WelcomeCard(employeeCount: Int) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(18.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Groups,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(14.dp),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Registro de empleados",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (employeeCount == 1) {
                        "1 persona registrada"
                    } else {
                        "$employeeCount personas registradas"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

@Composable
private fun EmployeeForm(
    onAddEmployee: (String, String, String, Double, String) -> Unit,
) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var position by rememberSaveable { mutableStateOf("") }
    var department by rememberSaveable { mutableStateOf("") }
    var salary by rememberSaveable { mutableStateOf("") }
    var selectedDateMillis by rememberSaveable { mutableLongStateOf(0L) }
    var validationAttempted by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val parsedSalary = salary.replace(',', '.').toDoubleOrNull()
    val fullNameError = validationAttempted && fullName.isBlank()
    val positionError = validationAttempted && position.isBlank()
    val departmentError = validationAttempted && department.isBlank()
    val salaryError = validationAttempted && (parsedSalary == null || parsedSalary <= 0.0)
    val dateError = validationAttempted && selectedDateMillis == 0L

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            val wideLayout = maxWidth >= 600.dp

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "Nuevo empleado",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }

                Text(
                    text = "Completá los datos para sumar una persona al equipo.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nombre completo") },
                    singleLine = true,
                    isError = fullNameError,
                    supportingText = if (fullNameError) {
                        { Text("Ingresá el nombre completo") }
                    } else {
                        null
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) },
                    ),
                )

                if (wideLayout) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        PositionField(
                            value = position,
                            onValueChange = { position = it },
                            isError = positionError,
                            modifier = Modifier.weight(1f),
                        )
                        DepartmentField(
                            value = department,
                            onValueChange = { department = it },
                            isError = departmentError,
                            modifier = Modifier.weight(1f),
                        )
                    }
                } else {
                    PositionField(
                        value = position,
                        onValueChange = { position = it },
                        isError = positionError,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    DepartmentField(
                        value = department,
                        onValueChange = { department = it },
                        isError = departmentError,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                if (wideLayout) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top,
                    ) {
                        SalaryField(
                            value = salary,
                            onValueChange = { salary = it },
                            isError = salaryError,
                            modifier = Modifier.weight(1f),
                        )
                        HireDateField(
                            selectedDateMillis = selectedDateMillis,
                            onDateSelected = { selectedDateMillis = it },
                            isError = dateError,
                            modifier = Modifier.weight(1f),
                        )
                    }
                } else {
                    SalaryField(
                        value = salary,
                        onValueChange = { salary = it },
                        isError = salaryError,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    HireDateField(
                        selectedDateMillis = selectedDateMillis,
                        onDateSelected = { selectedDateMillis = it },
                        isError = dateError,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Button(
                    onClick = {
                        validationAttempted = true
                        if (
                            fullName.isNotBlank() &&
                            position.isNotBlank() &&
                            department.isNotBlank() &&
                            parsedSalary != null &&
                            parsedSalary > 0.0 &&
                            selectedDateMillis != 0L
                        ) {
                            onAddEmployee(
                                fullName.trim(),
                                position.trim(),
                                department.trim(),
                                parsedSalary,
                                formatDate(selectedDateMillis),
                            )
                            fullName = ""
                            position = ""
                            department = ""
                            salary = ""
                            selectedDateMillis = 0L
                            validationAttempted = false
                            focusManager.clearFocus()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 14.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Registrar empleado")
                }
            }
        }
    }
}

@Composable
private fun PositionField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text("Cargo") },
        singleLine = true,
        isError = isError,
        supportingText = if (isError) ({ Text("Ingresá el cargo") }) else null,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    )
}

@Composable
private fun DepartmentField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text("Departamento") },
        singleLine = true,
        isError = isError,
        supportingText = if (isError) ({ Text("Ingresá el departamento") }) else null,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    )
}

@Composable
private fun SalaryField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.all { it.isDigit() || it == ',' || it == '.' }) {
                onValueChange(newValue)
            }
        },
        modifier = modifier,
        label = { Text("Salario") },
        prefix = { Text("₲ ") },
        singleLine = true,
        isError = isError,
        supportingText = if (isError) ({ Text("Ingresá un salario válido") }) else null,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HireDateField(
    selectedDateMillis: Long,
    onDateSelected: (Long) -> Unit,
    isError: Boolean,
    modifier: Modifier,
) {
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = "Fecha de contratación",
            style = MaterialTheme.typography.bodySmall,
            color = if (isError) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp),
        )
        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (selectedDateMillis == 0L) {
                    "Seleccionar fecha"
                } else {
                    formatDate(selectedDateMillis)
                },
            )
        }
        if (isError) {
            Text(
                text = "Seleccioná una fecha",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
            )
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis.takeIf { it != 0L }
                ?: System.currentTimeMillis(),
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let(onDateSelected)
                        showDatePicker = false
                    },
                    enabled = datePickerState.selectedDateMillis != null,
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun EmployeeListHeader(employeeCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Empleados registrados",
            style = MaterialTheme.typography.titleLarge,
        )
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(50),
        ) {
            Text(
                text = employeeCount.toString(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            )
        }
    }
}

@Composable
private fun EmptyEmployeeList() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Groups,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(42.dp),
            )
            Text(
                text = "Todavía no hay empleados",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "Completá el formulario para crear el primer registro.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun EmployeeCard(
    employee: Employee,
    onDelete: () -> Unit,
) {
    val details = listOf(
        EmployeeDetail(Icons.Default.Badge, "Cargo", employee.position),
        EmployeeDetail(Icons.Default.Business, "Departamento", employee.department),
        EmployeeDetail(Icons.Default.Payments, "Salario", formatSalary(employee.salary)),
        EmployeeDetail(Icons.Default.CalendarMonth, "Contratación", employee.hireDate),
    )

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = employee.fullName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(details) { detail ->
                    EmployeeDetailChip(detail = detail)
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            FilledTonalButton(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.End),
            ) {
                Icon(
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Eliminar")
            }
        }
    }
}

@Composable
private fun EmployeeDetailChip(detail: EmployeeDetail) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(14.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = detail.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp),
            )
            Column {
                Text(
                    text = detail.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = detail.value,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

private data class EmployeeDetail(
    val icon: ImageVector,
    val label: String,
    val value: String,
)

private fun formatDate(epochMillis: Long): String =
    Instant.ofEpochMilli(epochMillis)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()
        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

private fun formatSalary(salary: Double): String =
    NumberFormat.getCurrencyInstance(Locale("es", "PY")).format(salary)

@Preview(showBackground = true, widthDp = 420, heightDp = 900)
@Composable
private fun EmployeeScreenPreview() {
    RegistroEmpleadosTheme(darkTheme = false) {
        EmployeeScreen(
            employees = listOf(
                Employee(
                    id = 1,
                    fullName = "Ana Martínez",
                    position = "Diseñadora UX",
                    department = "Producto",
                    salary = 8_500_000.0,
                    hireDate = "15/03/2024",
                ),
            ),
            darkTheme = false,
            onThemeChange = {},
            onAddEmployee = { _, _, _, _, _ -> },
            onDeleteEmployee = {},
        )
    }
}
