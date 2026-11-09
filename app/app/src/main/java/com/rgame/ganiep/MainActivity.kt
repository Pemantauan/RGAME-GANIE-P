
package com.rgame.ganiep

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*

class MainActivity : Activity() {

    private val bg = Color.rgb(10, 10, 14)
    private val panel = Color.rgb(24, 22, 29)
    private val red = Color.rgb(255, 45, 75)

    private lateinit var content: LinearLayout
    private var currentPage = "Home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        showShell()
        showHome()
    }

    private fun rounded(color: Int, radius: Float = 18f) =
        GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius
        }

    private fun label(
        text: String,
        size: Float,
        color: Int = Color.WHITE,
        bold: Boolean = false
    ) = TextView(this).apply {
        this.text = text
        textSize = size
        setTextColor(color)
        if (bold) setTypeface(null, Typeface.BOLD)
    }

    private fun card(title: String, subtitle: String): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = rounded(panel)
            setPadding(16, 16, 16, 16)

            addView(label(title, 16f, Color.WHITE, true))

            addView(label(subtitle, 12f, Color.LTGRAY).apply {
                setPadding(0, 7, 0, 0)
            })

            layoutParams = LinearLayout.LayoutParams(-1, -2).apply {
                bottomMargin = 12
            }
        }
    }

    private fun showShell() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
            setPadding(18, 18, 18, 18)
        }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val brand = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, -2, 1f)
        }

        brand.addView(label("R  RGAME GANIE P", 20f, Color.WHITE, true))
        brand.addView(label("GAMING CONTROL CENTER", 9f, red, true))

        header.addView(brand)
        header.addView(label("● READY", 10f, Color.GREEN, true))
        root.addView(header)

        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 18, 0, 16)
        }

        listOf("Home", "Games", "Tools", "Settings").forEach { name ->
            val button = Button(this).apply {
                text = name
                textSize = 10f
                isAllCaps = false
                setTextColor(Color.WHITE)
                background = rounded(
                    if (currentPage == name) red else panel,
                    14f
                )

                setOnClickListener {
                    currentPage = name
                    showShell()
                    when (name) {
                        "Home" -> showHome()
                        "Games" -> showGames()
                        "Tools" -> showTools()
                        "Settings" -> showSettings()
                    }
                }
            }

            nav.addView(button, LinearLayout.LayoutParams(0, 44, 1f).apply {
                marginEnd = 5
            })
        }

        root.addView(nav)

        val scroll = ScrollView(this)
        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        setContentView(root)
    }

    private fun pageTitle(title: String, subtitle: String) {
        content.addView(label(title, 25f, Color.WHITE, true))
        content.addView(label(subtitle, 12f, Color.LTGRAY).apply {
            setPadding(0, 5, 0, 18)
        })
    }

    private fun showHome() {
        content.removeAllViews()
        pageTitle("Welcome back.", "Your gaming space. Tuned by you.")

        val hero = card(
            "RGAME / CONTROL HUB",
            "Versi awal • Local mode • Android"
        )

        hero.addView(label("PLAY SMARTER", 25f, red, true).apply {
            setPadding(0, 16, 0, 4)
        })

        hero.addView(label(
            "Kelola game dan akses fitur gaming dari satu tempat.",
            12f,
            Color.LTGRAY
        ))

        content.addView(hero)
        content.addView(card("GAME LIBRARY", "Deteksi aplikasi di perangkat."))
        content.addView(card("QUICK ACCESS", "Buka game dengan cepat."))
        content.addView(card("LOCAL SETTINGS", "Preferensi tersimpan di perangkat."))

        content.addView(label(
            "Fitur overlay dan monitor performa masih dalam pengembangan.",
            11f,
            Color.GRAY
        ))
    }

    @Suppress("DEPRECATION")
    private fun installedApps(): List<android.content.pm.ResolveInfo> {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        return if (Build.VERSION.SDK_INT >= 33) {
            packageManager.queryIntentActivities(
                intent,
                PackageManager.ResolveInfoFlags.of(0)
            )
        } else {
            packageManager.queryIntentActivities(intent, 0)
        }
    }

    private fun showGames() {
        content.removeAllViews()
        pageTitle("Game library", "Aplikasi yang tersedia di perangkat.")

        val apps = installedApps()
            .filter { it.activityInfo.packageName != packageName }
            .sortedBy { it.loadLabel(packageManager).toString().lowercase() }

        content.addView(label(
            "${apps.size} aplikasi terdeteksi",
            13f,
            red,
            true
        ))

        apps.forEach { info ->
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                background = rounded(panel)
                setPadding(12, 12, 12, 12)
            }

            val icon = ImageView(this).apply {
                setImageDrawable(info.loadIcon(packageManager))
            }

            row.addView(icon, LinearLayout.LayoutParams(44, 44))

            val name = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(12, 0, 0, 0)
                layoutParams = LinearLayout.LayoutParams(0, -2, 1f)
            }

            name.addView(label(
                info.loadLabel(packageManager).toString(),
                14f,
                Color.WHITE,
                true
            ))

            name.addView(label(
                info.activityInfo.packageName,
                9f,
                Color.GRAY
            ))

            row.addView(name)

            val open = Button(this).apply {
                text = "OPEN"
                textSize = 10f
                isAllCaps = false
                setTextColor(Color.WHITE)
                background = rounded(red, 12f)

                setOnClickListener {
                    packageManager.getLaunchIntentForPackage(
                        info.activityInfo.packageName
                    )?.let { startActivity(it) }
                }
            }

            row.addView(open, LinearLayout.LayoutParams(-2, 42))

            content.addView(row, LinearLayout.LayoutParams(-1, -2).apply {
                bottomMargin = 9
            })
        }
    }

    private fun showTools() {
        content.removeAllViews()
        pageTitle("Gaming tools", "Pusat fitur RGAME GANIE P.")

        content.addView(card(
            "Performance monitor",
            "FPS / RAM / CPU / GPU / suhu — belum aktif."
        ))

        content.addView(card(
            "Screenshot & screen recording",
            "Fitur perekaman dalam pengembangan."
        ))

        content.addView(card(
            "Custom crosshair",
            "Pengaturan tampilan bidik visual."
        ))

        content.addView(card(
            "Game timer & quick apps",
            "Fitur lanjutan dalam pengembangan."
        ))
    }

    private fun showSettings() {
        content.removeAllViews()
        pageTitle("Settings", "Pengaturan lokal aplikasi.")

        val prefs = getSharedPreferences(
            "rgame_settings",
            MODE_PRIVATE
        )

        content.addView(card("Tampilan", "Tema Red Cyber"))

        content.addView(label(
            "Preferensi transparansi",
            12f,
            Color.LTGRAY
        ))

        val seek = SeekBar(this).apply {
            max = 100
            progress = prefs.getInt("opacity", 85)

            setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        bar: SeekBar?,
                        value: Int,
                        fromUser: Boolean
                    ) {
                        if (fromUser) {
                            prefs.edit()
                                .putInt("opacity", value)
                                .apply()
                        }
                    }

                    override fun onStartTrackingTouch(bar: SeekBar?) {}
                    override fun onStopTrackingTouch(bar: SeekBar?) {}
                }
            )
        }

        content.addView(seek)

        content.addView(card(
            "Versi aplikasi",
            "RGAME GANIE P v0.1.0 • Prototype"
        ))

        content.addView(card(
            "Privasi",
            "Tidak memerlukan akun. Pengaturan disimpan lokal."
        ))
    }
}
