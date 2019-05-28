package com.example.furnitures.calculator.trick

object RepositoryFactory {

    // Nur eine einzige Instanz in dem gesamten Project, soll an mehrere Klassen weitergegeben werden
    // Repository als Interface: Es soll die Möglichkeit bestehen, die Repository für z.B. Testing ändern und austauschen zu können
    // Über die Factory wird immer nur genau eine Instanz erzeugt, welche aber auch über die Factory ausgetauscht werden kann
    // Man übergibt die Implementierung, zeigt die aber nur das Interface an, da es die Klassen nichts angeht, was die Implementierung ist

    private val furnitureRepositoryInterface: FurnitureRepository = FurnitureRepositoryImp()
    fun getFurnitureRepository() = furnitureRepositoryInterface
}