package me.scolastico.tools.web.admin.etc

data class AdminPanelConfig(
    var secureCookie:Boolean = false,
    var user: HashMap<String, String> = HashMap(),
    var staticTokens: HashMap<String, String> = HashMap(),
    var permissions: HashMap<String, ArrayList<String>> = HashMap()
)
