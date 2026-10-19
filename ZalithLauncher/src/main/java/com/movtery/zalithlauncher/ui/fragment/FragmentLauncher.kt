// Пример для FragmentLauncher.kt

// Кнопка запуска игры
binding.btnPlay.setOnClickListener {
    // Здесь вызывается метод запуска Minecraft (например, ваш метод старта игры)
    startGame()
}

// Карточка: Версии
binding.cardVersions.setOnClickListener {
    findNavController().navigate(R.id.action_launcher_to_versions)
}

// Карточка: Скин
binding.cardSkin.setOnClickListener {
    findNavController().navigate(R.id.action_launcher_to_skin)
}

// Карточка: Настройки
binding.cardSettings.setOnClickListener {
    findNavController().navigate(R.id.action_launcher_to_settings)
}

// Карточка: Скачивание (Modrinth / CurseForge)
binding.cardDownload.setOnClickListener {
    findNavController().navigate(R.id.action_launcher_to_download)
}
