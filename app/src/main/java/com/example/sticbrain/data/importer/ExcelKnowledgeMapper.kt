package com.example.sticbrain.data.importer

import com.example.sticbrain.data.local.entity.IncidenciaEntity

/**
 * Mapper para convertir una fila de Excel en una entidad de Room.
 */
fun ExcelKnowledgeRow.toIncidenciaEntity(): IncidenciaEntity {
    return IncidenciaEntity(
        categoria = this.categoria,
        tituloNombre = this.tituloNombre,
        descripcion = this.descripcion,
        frasesUsuario = this.frasesUsuario,
        procedimientoRespuesta = this.procedimientoRespuesta,
        palabrasClave = this.palabrasClave,
        nivelPrioridad = this.nivelPrioridad,
        notasComentarios = this.notasComentarios,
        fechaCreacion = System.currentTimeMillis(),
        fechaModificacion = null
    )
}
