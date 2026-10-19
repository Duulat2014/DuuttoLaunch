package com.movtery.zalithlauncher.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.movtery.zalithlauncher.R
import com.movtery.zalithlauncher.databinding.FragmentLauncherBinding // Замените на ваш класс биндинга, если он называется иначе

class FragmentLauncher : Fragment(R.layout.fragment_launcher) {

    private var _binding: FragmentLauncherBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLauncherBinding.bind(view)

        // Кнопка запуска игры
        binding.btnPlay.setOnClickListener {
            // Здесь вызывается стандартный метод запуска игры или перехода к старту
        }

        // Карточка: Версии
        binding.cardVersions.setOnClickListener {
            findNavController().navigate(R.id.nav_versions)
        }

        // Карточка: Скин
        binding.cardSkin.setOnClickListener {
            findNavController().navigate(R.id.nav_skin)
        }

        // Карточка: Настройки
        binding.cardSettings.setOnClickListener {
            findNavController().navigate(R.id.nav_settings)
        }

        // Карточка: Скачивание
        binding.cardDownload.setOnClickListener {
            findNavController().navigate(R.id.nav_download)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
