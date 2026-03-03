# MikuszPlanner

A personalized planner app for people with ADHD/autism. Break big tasks into manageable steps, celebrate every win with confetti, and let AI do the planning for you.

---

## Features

- **Main tasks + sub-tasks** — Create a task and break it into 3–5 smaller steps
- **Confetti explosion** — Every completed task or sub-task triggers a satisfying confetti burst
- **DeepSeek AI** — Automatically generate sub-tasks from your main task title using the DeepSeek API
- **5 unique themes** — Switch the entire app appearance between:
  - 🖥 **Windows 9x** — Classic gray Windows 95/98 aesthetic
  - 🌈 **Windows XP Luna** — Colorful gradient Luna theme
  - 💧 **Frutiger Aero** — Glossy glass and sky-blue nature vibes
  - 📓 **Notebook Cards** — Lined paper, post-it notes and ink
  - 🎵 **iPod YouTube** — Dark YouTube-on-iPod 2007 style

---

## Setting Up Your DeepSeek API Key

The AI sub-task generation feature requires a DeepSeek API key. Without it, you can still use the app normally — just add tasks and steps manually.

### Step 1 — Get a DeepSeek API key

1. Go to [platform.deepseek.com](https://platform.deepseek.com)
2. Create an account or sign in
3. Navigate to **API Keys** in your dashboard
4. Click **Create new API key**
5. Copy the key — it starts with `sk-`

### Step 2 — Enter the key in the app

1. Open **MikuszPlanner**
2. Tap the **palette icon** (🎨) in the top-right corner of the home screen
3. Scroll to the bottom of the Appearance screen
4. Tap **DeepSeek API Key**
5. Paste your key into the field and tap **Save**

Your key is stored locally on your device using Android DataStore — it is never sent anywhere except directly to the DeepSeek API when you request AI generation.

### Step 3 — Generate sub-tasks with AI

1. Create a main task (e.g. *"Clean my room"*)
2. Tap the task to open its detail screen
3. Tap **"Generate steps with DeepSeek AI"**
4. The AI will generate 3–5 specific, actionable steps and add them automatically

---

## Building the Project

### Requirements

- Android Studio Meerkat or later
- JDK 11+
- Android SDK with API 36

### Steps

```bash
git clone https://github.com/youruser/MikuszPlanner.git
cd MikuszPlanner
```

Open the project in Android Studio and click **Sync Project with Gradle Files**. Then run on an emulator or physical device.

> **Emulator tip:** Wait for the home screen to fully appear before clicking Run, otherwise the install may fail with a Package Manager error.

---

## Project Structure

```
app/src/main/java/com/mikusz3/mikuszplanner/
├── data/
│   ├── api/          # DeepSeek Retrofit service
│   ├── db/           # Room database & DAO
│   ├── model/        # Task, SubTask, TaskWithSubTasks
│   ├── preferences/  # DataStore (theme + API key)
│   └── repository/   # TaskRepository
├── viewmodel/        # TaskViewModel, ThemeViewModel
└── ui/
    ├── components/   # ThemedComponents, TaskCard, SubTaskItem, ConfettiOverlay
    ├── navigation/   # AppNavigation
    ├── screens/      # HomeScreen, TaskDetailScreen, ThemePickerScreen
    └── theme/        # AppTheme enum, Color, Type, Theme (5 color schemes)
```

---

## Privacy

- Your API key is stored **only on your device** using Android's DataStore
- No data is collected or sent to any third-party server
- The only external network request made is to `api.deepseek.com` when you explicitly tap the AI generation button

---

## License

MIT
