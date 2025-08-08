package com.example.wearos_tile.tile

import android.content.Context
import androidx.wear.tiles.*
import androidx.wear.tiles.material.Text
import androidx.wear.tiles.material.Typography
import androidx.wear.tiles.material.layouts.PrimaryLayout
import com.example.wearos_tile.data.ImageProvider
import com.google.android.horologist.tiles.CoroutinesTileService
import java.io.File
import androidx.wear.tiles.LayoutElementBuilders.Box
import androidx.wear.tiles.LayoutElementBuilders.CONTENT_SCALE_MODE_CROP
import androidx.wear.tiles.LayoutElementBuilders.Image
import androidx.wear.tiles.ModifiersBuilders.Clickable

private const val RESOURCES_VERSION = "1"
private const val REFRESH_ACTION_ID = "refresh_action"
private const val IMAGE_RESOURCE_ID = "image_resource"

class ImageTileService : CoroutinesTileService() {

    override suspend fun resourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ResourceBuilders.Resources {
        val imageFile = ImageProvider.getRandomImage()
        val resourceBuilder = ResourceBuilders.Resources.Builder()
            .setVersion(RESOURCES_VERSION)

        if (imageFile != null) {
            val imageBytes = imageFile.readBytes()
            val imageResource = ResourceBuilders.ImageResource.Builder()
                .setData(imageBytes)
                .build()
            resourceBuilder.addIdToImageMapping(IMAGE_RESOURCE_ID, imageResource)
        }

        return resourceBuilder.build()
    }

    override suspend fun tileRequest(requestParams: RequestBuilders.TileRequest): TileBuilders.Tile {
        // This is called when the tile is rendered.
        // We call getRandomImage() again to decide which layout to use.
        // This is slightly inefficient but ensures the tile content is consistent.
        val imageFile = ImageProvider.getRandomImage()

        val layout = if (imageFile != null) {
            // Se viene trovata un'immagine, la mostra a schermo intero
            imageLayout(this, IMAGE_RESOURCE_ID)
        } else {
            // Altrimenti, mostra un messaggio di fallback
            noImageLayout(this)
        }

        return TileBuilders.Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(TimelineBuilders.Timeline.fromLayoutElement(layout))
            .build()
    }
}

/**
 * Crea un layout che mostra un'immagine a schermo intero.
 * L'immagine viene ritagliata per riempire lo schermo (crop center).
 * Un'azione di clic è associata per aggiornare la tile.
 */
private fun imageLayout(context: Context, imageResourceId: String): LayoutElementBuilders.LayoutElement {
    return Box.Builder()
        .setWidth(DimensionBuilders.expand())
        .setHeight(DimensionBuilders.expand())
        .addContent(
            Image.Builder()
                .setResourceId(imageResourceId)
                .setWidth(DimensionBuilders.expand())
                .setHeight(DimensionBuilders.expand())
                .setContentScaleMode(CONTENT_SCALE_MODE_CROP)
                .build()
        )
        .setModifiers(
            ModifiersBuilders.Modifiers.Builder()
                .setClickable(
                    Clickable.Builder()
                        .setOnClick(ActionBuilders.LoadAction.Builder().build())
                        .setId(REFRESH_ACTION_ID)
                        .build()
                )
                .build()
        )
        .build()
}

/**
 * Crea un layout che mostra un messaggio quando non vengono trovate immagini.
 */
private fun noImageLayout(context: Context): LayoutElementBuilders.LayoutElement {
    return PrimaryLayout.Builder(DeviceParametersBuilders.DeviceParameters.Builder().build())
        .setContent(
            Text.Builder(context, context.getString(R.string.no_images_found))
                .setTypography(Typography.TYPOGRAPHY_BODY1)
                .build()
        )
        .build()
}
