package akhmedoff.usman.videoforvk.model

class Catalog(
    var items: List<VideoCatalog>,
    var name: String,
    var id: String,
    var view: String,
    var canHide: Boolean,
    var type: String?
)