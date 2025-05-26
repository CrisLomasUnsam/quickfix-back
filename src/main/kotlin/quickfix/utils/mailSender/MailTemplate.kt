package quickfix.utils.mailSender

abstract class MailTemplate {
    fun buildHtml(bodyContent: String): String {
        return """
            <html>
                <body style=\"font-family:sans-serif;\">
                    <header><h2>QuickFix</h2></header>
                    $bodyContent
                    <footer><p>Gracias por usar QuickFix.</p></footer>
                </body>
            </html>
        """.trimIndent()
    }
}