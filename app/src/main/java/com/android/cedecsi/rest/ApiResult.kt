package com.android.cedecsi.rest

data class ApiResult (
    var success: Boolean,
    var source: String,
    var data: Validate,
    var message: String?
)

data class Validate (
    var direccion: String, //: "",
    var direccion_completa: String, //: "",
    var ruc: String, //: "10469806770",
    var nombre_o_razon_social: String, //: "COJAL MEDINA WALTER SANTIAGO",
    var estado: String, //: "ACTIVO",
    var condicion: String, //: "HABIDO",
    var departamento: String, //: "",
    var provincia: String, //: "",
    var distrito: String, //: "",
    var ubigeo_sunat: String, //: "",
    var es_agente_de_retencion: String, //: "NO",
    var ubigeo: List<String>
)