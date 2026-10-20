package com.movtery.zalithlauncher.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.movtery.anim.AnimPlayer
import com.movtery.anim.animations.Animations
import com.movtery.zalithlauncher.R
import com.movtery.zalithlauncher.databinding.FragmentLauncherBinding
import com.movtery.zalithlauncher.event.single.AccountUpdateEvent
import com.movtery.zalithlauncher.event.single.LaunchGameEvent
import com.movtery.zalithlauncher.event.single.RefreshVersionsEvent
import com.movtery.zalithlauncher.feature.version.Version
import com.movtery.zalithlauncher.feature.version.VersionsManager
import com.movtery.zalithlauncher.ui.subassembly.account.AccountViewWrapper
import com.movtery.zalithlauncher.utils.ZHTools
import net.kdt.pojavlaunch.progresskeeper.ProgressKeeper
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainMenuFragment : FragmentWithAnim(R.layout.fragment_launcher) {
    companion object {
        const val TAG = "MainMenuFragment"
    }

    private var binding: FragmentLauncherBinding? = null
    private lateinit var accountViewWrapper: AccountViewWrapper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLauncherBinding.inflate(inflater, container, false)
        accountViewWrapper = AccountViewWrapper(this, binding!!.viewAccount)
        accountViewWrapper.refreshAccountInfo()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val b = binding ?: return

        // Кнопка запуска игры
        b.btnPlay.setOnClickListener { EventBus.getDefault().post(LaunchGameEvent()) }

        // Карточка: Версии
        b.cardVersions.setOnClickListener {
            if (!listTaskRunning()) {
                ZHTools.swapFragmentWithAnim(this, VersionsListFragment::class.java, VersionsListFragment.TAG, null)
            } else {
                Toast.makeText(requireContext(), R.string.version_manager_no_installed_version, Toast.LENGTH_SHORT).show()
            }
        }

        // Карточка: Скин
        b.cardSkin.setOnClickListener {
            if (!listTaskRunning()) {
                ZHTools.swapFragmentWithAnim(this, AccountFragment::class.java, AccountFragment.TAG, null)
            } else {
                Toast.makeText(requireContext(), R.string.version_manager_no_installed_version, Toast.LENGTH_SHORT).show()
            }
        }

        // Карточка: Настройки
        b.cardSettings.setOnClickListener {
            if (!listTaskRunning()) {
                ZHTools.swapFragmentWithAnim(this, SettingsFragment::class.java, SettingsFragment.TAG, null)
            } else {
                Toast.makeText(requireContext(), R.string.version_manager_no_installed_version, Toast.LENGTH_SHORT).show()
            }
        }

        // Карточка: Скачивание
        b.cardDownload.setOnClickListener {
            if (!listTaskRunning()) {
                ZHTools.swapFragmentWithAnim(this, DownloadFragment::class.java, DownloadFragment.TAG, null)
            } else {
                Toast.makeText(requireContext(), R.string.version_manager_no_installed_version, Toast.LENGTH_SHORT).show()
            }
        }

        b.versionName?.isSelected = true
        b.versionInfo?.isSelected = true

        refreshCurrentVersion()
    }

    private fun refreshCurrentVersion() {
        val b = binding ?: return
        val version: Version? = VersionsManager.getCurrentVersion()
        if (version == null) {
            b.versionName.setText(R.string.version_manager_no_installed_version)
            b.versionInfo.text = ""
            return
        }
        b.versionName.text = version.getVersionName()
        b.versionInfo.text = ""
    }

    private fun listTaskRunning(): Boolean {
        return ProgressKeeper.hasOngoingTasks()
    }

    override fun slideIn(animPlayer: AnimPlayer) {
        binding?.apply {
            animPlayer.apply(AnimPlayer.Entry(btnPlay, Animations.BounceInRight))
        }
    }

    override fun slideOut(animPlayer: AnimPlayer) {
        binding?.apply {
            animPlayer.apply(AnimPlayer.Entry(btnPlay, Animations.FadeOutLeft))
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAccountUpdateEvent(event: AccountUpdateEvent) {
        accountViewWrapper.refreshAccountInfo()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshVersionsEvent(event: RefreshVersionsEvent) {
        refreshCurrentVersion()
    }
}
