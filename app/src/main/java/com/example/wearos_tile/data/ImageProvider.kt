package com.example.wearos_tile.data

import android.os.Environment
import java.io.File

object ImageProvider {

    // Estensioni di file immagine supportate
    private val IMAGE_EXTENSIONS = listOf("jpg", "jpeg", "png")

    /**
     * Cerca ricorsivamente le immagini nella directory /sdcard/Pictures/ e ne restituisce una casuale.
     *
     * @return Un oggetto [File] che rappresenta un'immagine casuale, o null se non ne vengono trovate.
     */
    fun getRandomImage(): File? {
        // Ottiene la directory "Pictures" nella memoria esterna
        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        // Se la directory non esiste o non è una directory, non ci sono immagini da trovare
        if (!picturesDir.exists() || !picturesDir.isDirectory) {
            return null
        }

        // Cerca ricorsivamente tutti i file nella directory e nelle sue sottodirectory
        val imageFiles = picturesDir.walk()
            .filter { it.isFile && it.extension.lowercase() in IMAGE_EXTENSIONS }
            .toList()

        // Se non ci sono file immagine, restituisce null
        if (imageFiles.isEmpty()) {
            return null
        }

        // Restituisce un file immagine casuale dalla lista
        return imageFiles.random()
    }
}
