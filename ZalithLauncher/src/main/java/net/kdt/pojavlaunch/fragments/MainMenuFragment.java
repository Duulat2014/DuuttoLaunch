package net.kdt.pojavlaunch.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.movtery.zalithlauncher.databinding.FragmentLauncherBinding;
import com.movtery.zalithlauncher.event.single.AccountUpdateEvent;
import com.movtery.zalithlauncher.event.single.LaunchGameEvent;
import com.movtery.zalithlauncher.event.single.RefreshVersionsEvent;
import com.movtery.zalithlauncher.feature.version.Version;
import com.movtery.zalithlauncher.feature.version.VersionIconUtils;
import com.movtery.zalithlauncher.feature.version.VersionManager;
import com.movtery.zalithlauncher.ui.Animations;
import com.movtery.zalithlauncher.ui.fragment.AccountFragment;
import com.movtery.zalithlauncher.ui.fragment.DownloadFragment;
import com.movtery.zalithlauncher.ui.fragment.FragmentWithAnim;
import com.movtery.zalithlauncher.ui.fragment.SettingsFragment;
import com.movtery.zalithlauncher.ui.fragment.VersionsListFragment;
import com.movtery.zalithlauncher.ui.subassembly.account.AccountViewWrapper;
import com.movtery.zalithlauncher.utils.ZHTools;
import com.movtery.zalithlauncher.utils.anim.ViewAnimUtils;

import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.progresskeeper.ProgressKeeper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainMenuFragment extends FragmentWithAnim {
    public static final String TAG = "MainMenuFragment";
    private FragmentLauncherBinding binding;
    private AccountViewWrapper accountViewWrapper;

    public MainMenuFragment() {
        super(R.layout.fragment_launcher);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLauncherBinding.inflate(getLayoutInflater());
        accountViewWrapper = new AccountViewWrapper(this, binding.viewAccount);
        accountViewWrapper.refreshAccountInfo();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Кнопка запуска игры
        binding.btnPlay.setOnClickListener(v -> EventBus.getDefault().post(new LaunchGameEvent()));

        // Карточка: Версии
        binding.cardVersions.setOnClickListener(v -> {
            if (!listTaskRunning()) {
                ZHTools.swapFragmentWithAnim(this, VersionsListFragment.class, VersionsListFragment.TAG, null);
            } else {
                ViewAnimUtils.setViewAnim(binding.cardVersions, Animations.Shake);
                Toast.makeText(requireContext(), R.string.task_is_running, Toast.LENGTH_SHORT).show();
            }
        });

        // Карточка: Скин
        binding.cardSkin.setOnClickListener(v -> {
            if (!listTaskRunning()) {
                ZHTools.swapFragmentWithAnim(this, AccountFragment.class, AccountFragment.TAG, null);
            } else {
                ViewAnimUtils.setViewAnim(binding.cardSkin, Animations.Shake);
                Toast.makeText(requireContext(), R.string.task_is_running, Toast.LENGTH_SHORT).show();
            }
        });

        // Карточка: Настройки
        binding.cardSettings.setOnClickListener(v -> {
            if (!listTaskRunning()) {
                ZHTools.swapFragmentWithAnim(this, SettingsFragment.class, SettingsFragment.TAG, null);
            } else {
                ViewAnimUtils.setViewAnim(binding.cardSettings, Animations.Shake);
                Toast.makeText(requireContext(), R.string.task_is_running, Toast.LENGTH_SHORT).show();
            }
        });

        // Карточка: Скачивание
        binding.cardDownload.setOnClickListener(v -> {
            if (!listTaskRunning()) {
                ZHTools.swapFragmentWithAnim(this, DownloadFragment.class, DownloadFragment.TAG, null);
            } else {
                ViewAnimUtils.setViewAnim(binding.cardDownload, Animations.Shake);
                Toast.makeText(requireContext(), R.string.task_is_running, Toast.LENGTH_SHORT).show();
            }
        });

        if (binding.versionName != null) binding.versionName.setSelected(true);
        if (binding.versionInfo != null) binding.versionInfo.setSelected(true);

        refreshCurrentVersion();
    }

    private void refreshCurrentVersion() {
        Version version = VersionsManager.INSTANCE.getCurrentVersion();
        if(version == null) {
            binding.versionName.setText(R.string.global_no_version);
            binding.versionInfo.setText("");
            binding.versionIcon.setImageResource(R.drawable.ic_crafting_table);
            return;
        }
        binding.versionName.setText(version.name);
        binding.versionInfo.setText(VersionManager.INSTANCE.getVersionTypeString(version));
        VersionIconUtils.INSTANCE.loadVersionIcon(binding.versionIcon, version);
    }

    private boolean listTaskRunning() {
        return ProgressKeeper.hasTasks();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(AccountUpdateEvent event) {
        accountViewWrapper.refreshAccountInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(VersionsManager.VersionChangeEvent event) {
        refreshCurrentVersion();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(RefreshVersionsEvent event) {
        refreshCurrentVersion();
    }
}
