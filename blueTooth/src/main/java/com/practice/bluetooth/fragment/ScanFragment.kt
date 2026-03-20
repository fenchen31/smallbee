package com.practice.bluetooth.fragment

import android.Manifest
import android.bluetooth.BluetoothManager
import android.location.LocationManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.practice.blueTooth.R
import com.practice.blueTooth.databinding.FragmentScanBinding
import com.practice.bluetooth.event.ScanEvent
import com.practice.bluetooth.viewmodel.ScanViewModel
import com.practice.common.base.BaseFragment

class ScanFragment : BaseFragment<FragmentScanBinding>(R.layout.fragment_scan) {

    private val viewModel: ScanViewModel by viewModels()
    private val event by lazy { ScanEvent() }
    private val permissions by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        } else {
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        }
    }
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var locationLauncher: ActivityResultLauncher<String>

    override fun initBinding() {
        binding.event = event
        binding.vm = viewModel
    }

    override fun initView(arguments: HashMap<String, Any>?) {
        initPermission()
        initObservable()
    }

    private fun initObservable() {
        viewModel.checkPermission.observe(viewLifecycleOwner) {
            if (it) permissionLauncher.launch(permissions)
        }
        viewModel.openBlueToothAndLocation.observe(viewLifecycleOwner) {
            if (it && hasBlueToothAndLocationOpen()) viewModel.doNext(ScanViewModel.Step.START_SCAN)
        }
    }

    private fun hasBlueToothAndLocationOpen(): Boolean {
        val blueToothAdapter =
            requireContext().getSystemService(BluetoothManager::class.java).adapter
        if (blueToothAdapter == null) {
            Toast.makeText(
                requireContext(),
                getString(R.string.device_does_not_support_bluetooth),
                Toast.LENGTH_SHORT
            ).show()
        } else if (!blueToothAdapter.isEnabled) {
            Toast.makeText(
                requireContext(), getString(R.string.please_open_bluetooth), Toast.LENGTH_SHORT
            ).show()
        } else {
            val locationManager = requireContext().getSystemService(LocationManager::class.java)
            val gpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val networkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (gpsEnable || networkEnable) {
                return true
            } else {
                Toast.makeText(
                    requireContext(), getString(R.string.please_open_location), Toast.LENGTH_SHORT
                ).show()
            }
        }
        return false
    }

    private fun initPermission() {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(), { result ->
                val disagrees = result.filterValues { !it }.keys
                if (disagrees.isEmpty()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        locationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    } else {
                        viewModel.doNext(ScanViewModel.Step.OPEN_BLUETOOTH_LOCATION)
                    }
                } else {
                    val builder = StringBuilder()
                    when {
                        disagrees.contains(Manifest.permission.BLUETOOTH_SCAN) -> builder.append(
                            "蓝牙扫描、"
                        )

                        disagrees.contains(Manifest.permission.BLUETOOTH) -> builder.append(
                            "蓝牙、"
                        )

                        disagrees.contains(Manifest.permission.BLUETOOTH_ADMIN) -> builder.append(
                            "蓝牙管理、"
                        )

                        disagrees.contains(Manifest.permission.ACCESS_FINE_LOCATION) -> builder.append(
                            "精确定位、"
                        )

                        disagrees.contains(Manifest.permission.ACCESS_COARSE_LOCATION) -> builder.append(
                            "模糊定位"
                        )
                    }
                    Toast.makeText(requireContext(), "请允许${builder}权限", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        locationLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission(), { result ->
                if (result) {
                    viewModel.doNext(ScanViewModel.Step.OPEN_BLUETOOTH_LOCATION)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.please_give_location_permission),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

}