package com.erick.ballas.cazarpatos

import android.content.Context
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.IOException

class InternalStorageFileHandler(
    private val context: Context,
    private val defaultFileName: String = "default_app_data.txt" // Nombre de archivo por defecto para ReadInformation
) : FileHandler {

    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        val fileName = datosAGrabar.first
        val data = datosAGrabar.second
        try {
            val fos: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            fos.write(data.toByteArray())
            fos.close()
            // Podrías añadir logging aquí para confirmar el guardado
            // Log.d("InternalStorage", "Datos guardados en $fileName")
        } catch (e: IOException) {
            e.printStackTrace()
            // Considera cómo quieres manejar las excepciones.
            // ¿Lanzar una excepción personalizada? ¿Devolver un resultado?
        }
    }

    override fun ReadInformation(): Pair<String, String> {
        val stringBuilder = StringBuilder()
        var statusMessage = "Success" // Mensaje de estado por defecto

        try {
            val fis = context.openFileInput(defaultFileName)
            val inputStreamReader = InputStreamReader(fis)
            val bufferedReader = BufferedReader(inputStreamReader)
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append('\n')
            }
            fis.close()

            // Eliminar el último salto de línea si existe y el contenido no está vacío
            if (stringBuilder.isNotEmpty() && stringBuilder.last() == '\n') {
                stringBuilder.deleteCharAt(stringBuilder.length - 1)
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            statusMessage = "Error: Archivo '$defaultFileName' no encontrado."
            // stringBuilder se mantendrá vacío
        } catch (e: IOException) {
            e.printStackTrace()
            statusMessage = "Error: No se pudo leer el archivo '$defaultFileName'."
            // stringBuilder podría tener datos parciales, o podrías decidir limpiarlo.
            // Por simplicidad, aquí lo dejamos como esté en caso de error de IO post-apertura.
        }

        return Pair(stringBuilder.toString(), statusMessage)
    }
}