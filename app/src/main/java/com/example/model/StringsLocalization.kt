package com.example.model

object StringsLocalization {

  fun appTitle(): String = "DiceMaster"

  fun navHome(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Home"
    AppLanguage.FR -> "Accueil"
    AppLanguage.DE -> "Start"
  }

  fun navHistory(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "History"
    AppLanguage.FR -> "Historique"
    AppLanguage.DE -> "Verlauf"
  }

  fun navSettings(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Settings"
    AppLanguage.FR -> "Paramètres"
    AppLanguage.DE -> "Einstellungen"
  }

  fun totalBadge(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Total: "
    AppLanguage.FR -> "Total: "
    AppLanguage.DE -> "Gesamt: "
  }

  fun rollAllButton(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "ROLL ALL"
    AppLanguage.FR -> "TOUT LANCER"
    AppLanguage.DE -> "ALLE WÜRFELN"
  }

  fun clearDice(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Clear Dice"
    AppLanguage.FR -> "Effacer les dés"
    AppLanguage.DE -> "Würfel leeren"
  }

  fun resetDice(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Reset"
    AppLanguage.FR -> "Réinitialiser"
    AppLanguage.DE -> "Zurücksetzen"
  }

  fun addModifier(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Modifier"
    AppLanguage.FR -> "Modificateur"
    AppLanguage.DE -> "Modifikator"
  }

  // History Screen
  fun historyTitle(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Roll History"
    AppLanguage.FR -> "Historique des Lancers"
    AppLanguage.DE -> "Wurf-Verlauf"
  }

  fun historyEmpty(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "No rolls yet. Roll some dice to see them here!"
    AppLanguage.FR -> "Aucun lancer pour le moment. Lancez des dés pour les voir ici !"
    AppLanguage.DE -> "Noch keine Würfe. Würfeln Sie, um sie hier zu sehen!"
  }

  fun clearHistory(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Clear History"
    AppLanguage.FR -> "Effacer l'historique"
    AppLanguage.DE -> "Verlauf löschen"
  }

  fun totalLabel(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "TOTAL"
    AppLanguage.FR -> "TOTAL"
    AppLanguage.DE -> "GESAMT"
  }

  fun timeJustNow(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Just now"
    AppLanguage.FR -> "À l'instant"
    AppLanguage.DE -> "Gerade eben"
  }

  fun timeMinutesAgo(minutes: Long, lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> if (minutes <= 1) "1 min ago" else "$minutes mins ago"
    AppLanguage.FR -> if (minutes <= 1) "Il y a 1 min" else "Il y a $minutes min"
    AppLanguage.DE -> if (minutes <= 1) "Vor 1 Min" else "Vor $minutes Min"
  }

  fun timeHoursAgo(hours: Long, lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> if (hours <= 1) "1 hour ago" else "$hours hours ago"
    AppLanguage.FR -> if (hours <= 1) "Il y a 1 heure" else "Il y a $hours heures"
    AppLanguage.DE -> if (hours <= 1) "Vor 1 Stunde" else "Vor $hours Stunden"
  }

  fun timeYesterday(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Yesterday"
    AppLanguage.FR -> "Hier"
    AppLanguage.DE -> "Gestern"
  }

  // Settings Screen
  fun settingsTitle(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Settings & Personalization"
    AppLanguage.FR -> "Paramètres & Personnalisation"
    AppLanguage.DE -> "Einstellungen & Personalisierung"
  }

  fun settingsSubtitle(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Customize your DiceMaster experience."
    AppLanguage.FR -> "Personnalisez votre expérience DiceMaster."
    AppLanguage.DE -> "Passen Sie Ihr DiceMaster-Erlebnis an."
  }

  fun appearanceSection(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "APPEARANCE"
    AppLanguage.FR -> "APPARENCE"
    AppLanguage.DE -> "ERSCHEINUNGSBILD"
  }

  fun appTheme(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "App Theme"
    AppLanguage.FR -> "Thème de l'application"
    AppLanguage.DE -> "App-Design"
  }

  fun appThemeDesc(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Select your preferred lighting."
    AppLanguage.FR -> "Choisissez votre éclairage préféré."
    AppLanguage.DE -> "Wählen Sie Ihre bevorzugte Beleuchtung."
  }

  fun themeLight(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Light"
    AppLanguage.FR -> "Clair"
    AppLanguage.DE -> "Hell"
  }

  fun themeDark(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Dark"
    AppLanguage.FR -> "Sombre"
    AppLanguage.DE -> "Dunkel"
  }

  fun themeSystem(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "System"
    AppLanguage.FR -> "Système"
    AppLanguage.DE -> "System"
  }

  fun themePink(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Pink"
    AppLanguage.FR -> "Pink Girly"
    AppLanguage.DE -> "Pink"
  }

  fun themeMetal(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Cold Metal"
    AppLanguage.FR -> "Métal Froid"
    AppLanguage.DE -> "Kaltes Metall"
  }

  fun diceStylingSection(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "DICE STYLING"
    AppLanguage.FR -> "STYLE DES DÉS"
    AppLanguage.DE -> "WÜRFEL-STIL"
  }

  fun primaryDiceColor(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Primary Dice Color"
    AppLanguage.FR -> "Couleur principale des dés"
    AppLanguage.DE -> "Haupt-Würfelfarbe"
  }

  fun primaryDiceColorDesc(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Choose the color for your main dice set."
    AppLanguage.FR -> "Choisissez la couleur de votre jeu de dés principal."
    AppLanguage.DE -> "Wählen Sie die Farbe für Ihr Haupt-Würfelset."
  }

  fun diceMaterialTexture(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Dice Material Texture"
    AppLanguage.FR -> "Texture et Matériau"
    AppLanguage.DE -> "Würfel-Materialtextur"
  }

  fun diceMaterialTextureDesc(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "The visual finish applied to 3D dice."
    AppLanguage.FR -> "Le fini visuel appliqué aux dés 3D."
    AppLanguage.DE -> "Die visuelle Oberfläche für 3D-Würfel."
  }

  fun languageSettingsSection(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "LANGUAGE SETTINGS"
    AppLanguage.FR -> "PARAMÈTRES DE LANGUE"
    AppLanguage.DE -> "SPRACHEINSTELLUNGEN"
  }

  fun appLanguage(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "App Language"
    AppLanguage.FR -> "Langue de l'application"
    AppLanguage.DE -> "App-Sprache"
  }

  fun appLanguageDesc(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Choose your preferred language for the interface."
    AppLanguage.FR -> "Choisissez votre langue préférée pour l'interface."
    AppLanguage.DE -> "Wählen Sie Ihre bevorzugte Sprache für die Benutzeroberfläche."
  }

  fun audioFeedbackSection(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "AUDIO & FEEDBACK"
    AppLanguage.FR -> "AUDIO & RETOURS"
    AppLanguage.DE -> "AUDIO & FEEDBACK"
  }

  fun soundEffects(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Sound Effects"
    AppLanguage.FR -> "Effets Sonores"
    AppLanguage.DE -> "Soundeffekte"
  }

  fun soundEffectsDesc(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Play realistic rolling sounds."
    AppLanguage.FR -> "Jouer des sons de lancer réalistes."
    AppLanguage.DE -> "Realistische Würfelgeräusche abspielen."
  }

  fun hapticFeedback(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Haptic Feedback"
    AppLanguage.FR -> "Retour Haptique"
    AppLanguage.DE -> "Haptisches Feedback"
  }

  fun hapticFeedbackDesc(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Vibrate device on rolls."
    AppLanguage.FR -> "Vibrer l'appareil lors des lancers."
    AppLanguage.DE -> "Gerät beim Würfeln vibrieren lassen."
  }

  fun shakeToRoll(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Shake to Roll"
    AppLanguage.FR -> "Secouer pour lancer"
    AppLanguage.DE -> "Schütteln zum Würfeln"
  }

  fun shakeToRollDesc(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> "Shake your device to roll all dice."
    AppLanguage.FR -> "Secouez votre téléphone pour lancer tous les dés."
    AppLanguage.DE -> "Schütteln Sie das Gerät, um alle Würfel zu werfen."
  }
}
