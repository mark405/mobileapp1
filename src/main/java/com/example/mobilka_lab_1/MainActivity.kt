package com.example.mobilka_lab_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobilka_lab_1.ui.theme.Mobilkalab1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mobilkalab1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LazyColumn(modifier = Modifier.padding(innerPadding)) {
                        item {
                            FuelCalculator()
                        }
                        item {
                            FuelOilCalculator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FuelCalculator(modifier: Modifier = Modifier) {
    var h by remember { mutableStateOf("") }
    var c by remember { mutableStateOf("") }
    var s by remember { mutableStateOf("") }
    var n by remember { mutableStateOf("") }
    var o by remember { mutableStateOf("") }
    var w by remember { mutableStateOf("") }
    var a by remember { mutableStateOf("") }

    var kRs by remember { mutableStateOf(0.0) }
    var kRg by remember { mutableStateOf(0.0) }
    var dryComposition by remember { mutableStateOf("") }
    var flammableComposition by remember { mutableStateOf("") }
    var lowerHeatingValue by remember { mutableStateOf("") }
    var lowerHeatingValueDry by remember { mutableStateOf("") }
    var lowerHeatingValueFlammable by remember { mutableStateOf("") }
    var percentageSum by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Калькулятор для розрахунку складу сухої та горючої маси палива та\n" +
                "нижчої теплоти згоряння")

        OutlinedTextField(
            value = h,
            onValueChange = { h = it },
            label = { Text("Введіть H%") },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = c,
            onValueChange = { c = it },
            label = { Text("Введіть C%") },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = s,
            onValueChange = { s = it },
            label = { Text("Введіть S%") },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = n,
            onValueChange = { n = it },
            label = { Text("Введіть N%") },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = o,
            onValueChange = { o = it },
            label = { Text("Введіть O%") },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = w,
            onValueChange = { w = it },
            label = { Text("Введіть W%") },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = a,
            onValueChange = { a = it },
            label = { Text("Введіть A%") },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = {
                val hValue = h.toDoubleOrNull() ?: 0.0
                val cValue = c.toDoubleOrNull() ?: 0.0
                val sValue = s.toDoubleOrNull() ?: 0.0
                val nValue = n.toDoubleOrNull() ?: 0.0
                val oValue = o.toDoubleOrNull() ?: 0.0
                val wValue = w.toDoubleOrNull() ?: 0.0
                val aValue = a.toDoubleOrNull() ?: 0.0

                kRs = 100 / (100 - wValue)
                kRg = 100 / (100 - wValue - aValue)

                val hC = hValue * kRs
                val cC = cValue * kRs
                val sC = sValue * kRs
                val nC = nValue * kRs
                val oC = oValue * kRs
                val aC = aValue * kRs

                val hG = hValue * kRg
                val cG = cValue * kRg
                val sG = sValue * kRg
                val nG = nValue * kRg
                val oG = oValue * kRg

                val qRn = 339 * cValue + 1030 * hValue - 108.8 * (oValue - sValue) - 25 * wValue
                val qRnMj = qRn / 1000

                val qDry = 339 * cC + 1030 * hC - 108.8 * (oC - sC) - 25 * (wValue * kRs)
                val qFlammable = 339 * cG + 1030 * hG - 108.8 * (oG - sG) - 25 * (wValue * kRg)

                kRs = String.format("%.2f", kRs).toDouble()
                kRg = String.format("%.2f", kRg).toDouble()
                dryComposition = "HС: ${String.format("%.2f", hC)}%, CС: ${String.format("%.2f", cC)}%, SС: ${String.format("%.2f", sC)}%, NС: ${String.format("%.3f", nC)}%, OС: ${String.format("%.2f", oC)}%, AС: ${String.format("%.2f", aC)}%"
                flammableComposition = "HГ: ${String.format("%.2f", hG)}%, CГ: ${String.format("%.2f", cG)}%, SГ: ${String.format("%.2f", sG)}%, NГ: ${String.format("%.3f", nG)}%, OГ: ${String.format("%.2f", oG)}%"
                lowerHeatingValue = String.format("%.4f", qRnMj)
                lowerHeatingValueDry = String.format("%.4f", qDry / 1000)
                lowerHeatingValueFlammable = String.format("%.4f", qFlammable / 1000)
                percentageSum = calculatePercentageSum(
                    hValue,
                    cValue,
                    sValue,
                    nValue,
                    oValue,
                    wValue,
                    aValue
                )
                percentageSum = String.format("%.2f", percentageSum.toDouble())
                      },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text("Calculate")
        }

        Text("Коефіцієнт переходу від робочої до сухої маси: $kRs")
        Text("Коефіцієнт переходу від робочої до горючої маси: $kRg")
        Text("Склад сухої маси: $dryComposition")
        Text("Склад горючої маси: $flammableComposition")
        Text("Нижча теплота згоряння для робочої маси: $lowerHeatingValue МДж/кг")
        Text("Нижча теплота згоряння для сухої маси: $lowerHeatingValueDry МДж/кг")
        Text("Нижча теплота згоряння для горючої маси: $lowerHeatingValueFlammable МДж/кг")
        Text("Сума відсотків: $percentageSum%")
    }
}

@Composable
fun FuelOilCalculator(modifier: Modifier = Modifier) {
    var carbon by remember { mutableStateOf("") }
    var hydrogen by remember { mutableStateOf("") }
    var oxygen by remember { mutableStateOf("") }
    var sulfur by remember { mutableStateOf("") }
    var lowerHeatingValue by remember { mutableStateOf("") }
    var moisture by remember { mutableStateOf("") }
    var ash by remember { mutableStateOf("") }
    var vanadium by remember { mutableStateOf("") }

    var resultComposition by remember { mutableStateOf("") }
    var lowerHeatingValueResult by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Калькулятор для перерахунку елементарного складу та нижчої\n" +
                "теплоти згоряння")

        OutlinedTextField(
            value = carbon,
            onValueChange = { carbon = it },
            label = { Text("Введіть Carbon %") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = hydrogen,
            onValueChange = { hydrogen = it },
            label = { Text("Введіть Hydrogen %") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = oxygen,
            onValueChange = { oxygen = it },
            label = { Text("Введіть Oxygen %") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = sulfur,
            onValueChange = { sulfur = it },
            label = { Text("Введіть Sulfur %") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = lowerHeatingValue,
            onValueChange = { lowerHeatingValue = it },
            label = { Text("Введіть Lower Heating Value (MJ/kg)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = moisture,
            onValueChange = { moisture = it },
            label = { Text("Введіть Moisture %") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = ash,
            onValueChange = { ash = it },
            label = { Text("Введіть Ash %") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = vanadium,
            onValueChange = { vanadium = it },
            label = { Text("Введіть Vanadium (mg/kg)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = {
                val carbonValue = carbon.toDoubleOrNull() ?: 0.0
                val hydrogenValue = hydrogen.toDoubleOrNull() ?: 0.0
                val oxygenValue = oxygen.toDoubleOrNull() ?: 0.0
                val sulfurValue = sulfur.toDoubleOrNull() ?: 0.0
                val lowerHeatingValueValue = lowerHeatingValue.toDoubleOrNull() ?: 0.0
                val moistureValue = moisture.toDoubleOrNull() ?: 0.0
                val ashValue = ash.toDoubleOrNull() ?: 0.0
                val vanadiumValue = vanadium.toDoubleOrNull() ?: 0.0

                val adjustmentFactor = (100 - moistureValue - ashValue) / 100
                val carbonWork = carbonValue * adjustmentFactor
                val hydrogenWork = hydrogenValue * adjustmentFactor
                val oxygenWork = oxygenValue * adjustmentFactor
                val sulfurWork = sulfurValue * adjustmentFactor
                val ashWork = ashValue * (100 - moistureValue) / 100
                val vanadiumWork = vanadiumValue * (100 - moistureValue) / 100

                val Qdaf = lowerHeatingValueValue
                val lowerHeatingValueWork = Qdaf * (100 - moistureValue - ashValue) / 100 - 0.025 * moistureValue

                resultComposition = "C: ${String.format("%.2f", carbonWork)}%; H: ${String.format("%.2f", hydrogenWork)}%; S: ${String.format("%.2f", sulfurWork)}%; O: ${String.format("%.2f", oxygenWork)}%; Ash: ${String.format("%.2f", ashWork)}%; Vanadium: ${String.format("%.2f", vanadiumWork)} mg/kg"
                lowerHeatingValueResult = String.format("%.2f", lowerHeatingValueWork)
            },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text("Calculate")
        }

        Text("Склад робочої маси мазуту: $resultComposition")
        Text("Нижча теплота згоряння мазуту на робочу масу для робочої маси за заданим складом компонентів палива становить: $lowerHeatingValueResult МДж/кг")
    }
}

fun calculatePercentageSum(h: Double, c: Double, s: Double, n: Double, o: Double, w: Double, a: Double): String {
    return (h + c + s + n + o + w + a).toString()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Mobilkalab1Theme {
        Column {
            FuelCalculator()
            FuelOilCalculator()
        }
    }
}
