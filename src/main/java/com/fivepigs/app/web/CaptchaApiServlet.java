package com.fivepigs.app.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@WebServlet(name = "CaptchaApiServlet", urlPatterns = {"/captcha-api"})
public class CaptchaApiServlet extends HttpServlet {

    private static final long EXPIRE_MILLIS = 60_000L;

    private static final String LOGIN = "login";
    private static final String REGISTER = "register";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ctx = normalizeContext(req.getParameter("ctx"));
        if (ctx == null) {
            writeJson(resp, "{\"ok\":false,\"message\":\"Invalid context\"}");
            return;
        }

        HttpSession session = req.getSession();
        CaptchaData data = generateCaptcha();
        storeCaptcha(session, ctx, data.question, data.answer);

        writeJson(resp, "{\"ok\":true,\"question\":\"" + jsonEscape(data.question) + "\"}");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String ctx = normalizeContext(req.getParameter("ctx"));
        if (ctx == null) {
            writeJson(resp, "{\"ok\":false,\"message\":\"Invalid context\"}");
            return;
        }

        HttpSession session = req.getSession();
        String userAnswer = req.getParameter("answer");
        CaptchaCheckResult result = verifyCaptcha(session, ctx, userAnswer);

        if (result.ok) {
            session.setAttribute(key(ctx, "verified"), Boolean.TRUE);
            writeJson(resp, "{\"ok\":true,\"message\":\"Captcha verified\"}");
            return;
        }

        writeJson(resp, "{\"ok\":false,\"message\":\"" + jsonEscape(result.message) + "\"}");
    }

    public static void ensureCaptcha(HttpServletRequest req, String ctx) {
        HttpSession session = req.getSession();

        Long time = (Long) session.getAttribute(key(ctx, "time"));
        String question = (String) session.getAttribute(key(ctx, "question"));
        if (time == null || question == null || System.currentTimeMillis() - time > EXPIRE_MILLIS) {
            CaptchaData data = generateCaptcha();
            storeCaptcha(session, ctx, data.question, data.answer);
            question = data.question;
        }

        req.setAttribute("captchaQuestion", question);
        req.setAttribute("captchaCtx", ctx);
    }

    public static String requireVerified(HttpSession session, String ctx) {
        Boolean verified = (Boolean) session.getAttribute(key(ctx, "verified"));
        if (verified == null || !verified) {
            return "Please verify captcha before submitting the form.";
        }

        session.removeAttribute(key(ctx, "verified"));
        session.removeAttribute(key(ctx, "question"));
        session.removeAttribute(key(ctx, "answer"));
        session.removeAttribute(key(ctx, "time"));
        session.removeAttribute(key(ctx, "attempts"));
        return null;
    }

    private static CaptchaCheckResult verifyCaptcha(HttpSession session, String ctx, String userAnswer) {
        String answer = (String) session.getAttribute(key(ctx, "answer"));
        Long time = (Long) session.getAttribute(key(ctx, "time"));
        Integer attempts = (Integer) session.getAttribute(key(ctx, "attempts"));

        if (answer == null || time == null) {
            CaptchaData data = generateCaptcha();
            storeCaptcha(session, ctx, data.question, data.answer);
            return new CaptchaCheckResult(false, "Invalid captcha. A new captcha has been generated.");
        }

        if (System.currentTimeMillis() - time > EXPIRE_MILLIS) {
            CaptchaData data = generateCaptcha();
            storeCaptcha(session, ctx, data.question, data.answer);
            return new CaptchaCheckResult(false, "Captcha expired. Please click Refresh captcha.");
        }

        if (attempts == null) attempts = 0;
        if (attempts >= 3) {
            CaptchaData data = generateCaptcha();
            storeCaptcha(session, ctx, data.question, data.answer);
            return new CaptchaCheckResult(false, "Too many incorrect attempts. A new captcha has been generated.");
        }

        session.setAttribute(key(ctx, "attempts"), attempts + 1);

        if (userAnswer == null || !userAnswer.trim().equalsIgnoreCase(answer)) {
            session.setAttribute(key(ctx, "verified"), Boolean.FALSE);
            return new CaptchaCheckResult(false, "Incorrect captcha.");
        }

        return new CaptchaCheckResult(true, "OK");
    }

    private static void storeCaptcha(HttpSession session, String ctx, String question, String answer) {
        session.setAttribute(key(ctx, "question"), question);
        session.setAttribute(key(ctx, "answer"), answer);
        session.setAttribute(key(ctx, "time"), System.currentTimeMillis());
        session.setAttribute(key(ctx, "attempts"), 0);
        session.setAttribute(key(ctx, "verified"), Boolean.FALSE);
    }

    private static CaptchaData generateCaptcha() {
        Random rand = new Random();
        List<CaptchaData> pool = new ArrayList<>();

        int a = rand.nextInt(16) + 5;
        int b = rand.nextInt(10) + 1;
        pool.add(new CaptchaData(a + " + " + b + " = ?", String.valueOf(a + b)));

        int c = rand.nextInt(20) + 10;
        int d = rand.nextInt(9) + 1;
        pool.add(new CaptchaData(c + " - " + d + " = ?", String.valueOf(c - d)));

        int e = rand.nextInt(8) + 2;
        int f = rand.nextInt(8) + 2;
        pool.add(new CaptchaData(e + " x " + f + " = ?", String.valueOf(e * f)));

        String[] words = {"apple", "banana", "orange", "planet", "coding", "review"};
        String word = words[rand.nextInt(words.length)];
        int idx = rand.nextInt(word.length());
        pool.add(new CaptchaData("What is the " + (idx + 1) + ordinalSuffix(idx + 1) + " letter in '" + word + "'?", String.valueOf(word.charAt(idx))));

        pool.add(new CaptchaData("Which number is greater: 17 or 12?", "17"));
        pool.add(new CaptchaData("What day comes after Monday? (Enter number: 1=Sun, 2=Mon, 3=Tue)", "3"));
        pool.add(new CaptchaData("In the sequence 2, 4, 6, 8, what is the last even number?", "8"));

        return pool.get(rand.nextInt(pool.size()));
    }

    private static String ordinalSuffix(int n) {
        int mod100 = n % 100;
        if (mod100 >= 11 && mod100 <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    private static String normalizeContext(String ctx) {
        if (ctx == null) return null;
        String v = ctx.trim().toLowerCase();
        if (LOGIN.equals(v) || REGISTER.equals(v)) return v;
        return null;
    }

    private static String key(String ctx, String name) {
        return ctx + "_captcha_" + name;
    }

    private static void writeJson(HttpServletResponse resp, String body) throws IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json");
        resp.getWriter().write(body);
    }

    private static String jsonEscape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static class CaptchaData {
        private final String question;
        private final String answer;

        private CaptchaData(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }
    }

    private static class CaptchaCheckResult {
        private final boolean ok;
        private final String message;

        private CaptchaCheckResult(boolean ok, String message) {
            this.ok = ok;
            this.message = message;
        }
    }
}
