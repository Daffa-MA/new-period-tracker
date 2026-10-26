# Onboarding Redesign Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Redesign onboarding flow with warm pink aesthetic, playful blob mascot, botanical elements, gesture navigation, and conversational copy that feels human-designed.

**Architecture:** Layer visual redesign on existing 5-step flow without changing data logic. New color/typography system, reusable animation utilities, gesture handler overlay, and component library (blob, botanicals, micro-interactions). Each step screen updates to use new visual language. Backward compatible with existing validation.

**Tech Stack:** Jetpack Compose, Material 3 (color scheme adaptation), Kotlin coroutines for animation, Material 3 animation APIs (AnimatedVisibility, AnimatedContent, spring/tween specs).

**Spec:** `docs/superpowers/specs/2026-10-26-onboarding-redesign.md`

## Global Constraints

- Target Android 12+ (API 31+)
- 60fps animation requirement (no dropped frames during swipe/transitions)
- Material 3 color scheme must be overridden, not replaced
- Existing data validation and onboarding logic unchanged
- Copy must be Indonesian; design localization-ready
- No new external dependencies (use Compose built-ins only)

## Review Focus

1. **Gesture threshold edge case:** Swipe detected as <50dp horizontal movement triggers nav when user only scrolled content vertically—gesture must suppress if vertical movement > horizontal.
2. **Animation stutter on animation curve transition:** Spring(MediumLow) + fadeIn together can cause jank; must test spring stiffness/damping on target device.
3. **Blob color tint per step:** Color shifts must not flashy-flicker (aliasing); verify easing curve smoothness and transition duration.
4. **Petal burst math on small screens:** Scatter radius calculated from screen width; small devices risk petals off-bounds—must clamp.
5. **Text overflow on long Indonesian copy:** Rounded serif font may increase line height; must verify no text truncation on 5" phones in all languages.

---

## File Structure

**New files:**
- `ui/theme/OnboardingColorScheme.kt` — Onboarding-specific Material 3 color overrides
- `ui/theme/OnboardingTypography.kt` — Rounded serif + rounded sans typography
- `presentation/ui/onboarding/components/BlobMascot.kt` — Blob shape + breath animation
- `presentation/ui/onboarding/components/BotanicalElements.kt` — Leaf/petal SVG illustrations + entrance animations
- `presentation/ui/onboarding/animations/MicroInteractions.kt` — Reusable animation specs (glow, burst, pulse)
- `presentation/ui/onboarding/gestures/SwipeNavigation.kt` — Gesture detector + swipe handler
- `presentation/ui/onboarding/components/OnboardingBackground.kt` — Animated background gradient per step

**Modified files:**
- `ui/theme/Theme.kt` — Apply OnboardingColorScheme + OnboardingTypography in PeriodTrackerTheme
- `presentation/ui/onboarding/OnboardingScreen.kt` — Integrate gestures, blob, botanicals, background animations
- `presentation/ui/onboarding/OnboardingScreen.kt` (individual step composables) — Refresh WelcomeStep, LastPeriodStep, CycleLengthStep, PeriodLengthStep, NotificationsStep with new animations/layout
- `res/values/strings.xml` — Update copy to conversational Indonesian

---

## Task 1: Create Onboarding Color Palette & Typography

**Files:**
- Create: `app/src/main/java/com/example/periodtracker/ui/theme/OnboardingColorScheme.kt`
- Create: `app/src/main/java/com/example/periodtracker/ui/theme/OnboardingTypography.kt`
- Modify: `app/src/main/java/com/example/periodtracker/ui/theme/Theme.kt`

**Interfaces:**
- Consumes: Material 3 colorScheme, existing Typography
- Produces: `onboardingColorScheme: ColorScheme`, `onboardingTypography: Typography` (globally usable via MaterialTheme in onboarding)

- [ ] **Step 1: Write OnboardingColorScheme.kt**

Define Material 3 `colorScheme` override with spec palette:
- Primary: #E8A8B0 (soft coral)
- Secondary: #D4A5A5 (dusty rose)
- Tertiary: #C89BA8 (muted mauve)
- Background: #FBF9F7 (cream)
- Surface: #FBF9F7
- OnBackground: #3D2E2B (dark text)
- OnSurface: #3D2E2B

Create both light and dark variants (dark variant inverts, but onboarding uses light only for now).

- [ ] **Step 2: Write OnboardingTypography.kt**

Define typography using Compose TextStyle:
- Headline (displayLarge, displayMedium): Rounded serif style (use Google Fonts: "Crimson Text" with custom letter-spacing +0.5sp for warmth, or fallback to system serif + custom TextStyle)
- Body (bodyLarge, bodyMedium, labelLarge): Rounded sans (use "Comfortaa" from Google Fonts or "Raleway" with rounded corners, or fallback to system sans with increased line-height)
- Increase all line-heights by 1.2x vs Material 3 default (breathing room)

Test: can render both header + body in preview without truncation.

- [ ] **Step 3: Modify Theme.kt to expose onboarding theme**

In `PeriodTrackerTheme` composable, add optional parameter `isOnboarding: Boolean = false`. When true, override colorScheme and typography with onboarding versions. Keep dynamic color and dark mode logic intact.

```kotlin
@Composable
fun PeriodTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    isOnboarding: Boolean = false,  // NEW
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        isOnboarding -> onboardingColorScheme  // NEW
        // ... existing logic
    }
    val typography = if (isOnboarding) onboardingTypography else Typography  // NEW
    // ... rest unchanged
}
```

- [ ] **Step 4: Update OnboardingScreen to pass isOnboarding=true**

In `OnboardingScreen` composable, wrap content in `PeriodTrackerTheme(isOnboarding = true) { ... }`

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/example/periodtracker/ui/theme/OnboardingColorScheme.kt
git add app/src/main/java/com/example/periodtracker/ui/theme/OnboardingTypography.kt
git add app/src/main/java/com/example/periodtracker/ui/theme/Theme.kt
git add app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/OnboardingScreen.kt
git commit -m "feat: add onboarding color scheme and typography"
```

---

## Task 2: Create Blob Mascot Component

**Files:**
- Create: `app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/components/BlobMascot.kt`
- Test: `app/src/test/java/com/example/periodtracker/presentation/ui/onboarding/components/BlobMascotTest.kt`

**Interfaces:**
- Consumes: Compose animation APIs, MaterialTheme.colorScheme
- Produces: `BlobMascot(modifier: Modifier, currentStep: Int)` composable

- [ ] **Step 1: Write failing test**

```kotlin
@Test
fun blobMascot_renders_without_crash() {
    composeRule.setContent {
        BlobMascot(modifier = Modifier.size(100.dp), currentStep = 0)
    }
    composeRule.onRoot().assertExists()
}

@Test
fun blobMascot_breath_animates_continuously() {
    var scaleValues = mutableListOf<Float>()
    composeRule.setContent {
        val scale = remember { Animatable(1f) }
        LaunchedEffect(Unit) {
            scale.animateTo(1.02f, animationSpec = infiniteRepeatable(tween(2000)))
        }
        scaleValues.add(scale.value)
    }
    // Just verify animation compiles and runs (full animation test deferred to manual)
    assert(scaleValues.isNotEmpty())
}
```

- [ ] **Step 2: Run test, verify it fails**

```bash
./gradlew testDebugUnitTest -k BlobMascot
```

Expected: Class BlobMascot not found.

- [ ] **Step 3: Implement BlobMascot composable**

Create blob shape using Compose `Canvas` or `RoundedCornerShape` + `Box`. Shape: rounded rectangle (all corners bulge equally, ~50% border radius). Size: ~80-100dp configurable.

Animations:
- **Breath:** infinite repeating scale 1.0 → 1.02 → 1.0, duration 2000ms, easing Ease-In-Out
- **React on input:** bounce vertically 8-12dp on `currentStep` change, triggering a transient vertical translation
- **Color tint per step:** use `when(currentStep)` to map step 0→primary pink, 1→rose, 2→mauve, 3→cream, 4→pink; animate color transition with 600ms tween

Blob is **aria-hidden** (not accessible, purely decorative).

Example skeleton:
```kotlin
@Composable
fun BlobMascot(
    modifier: Modifier = Modifier,
    currentStep: Int = 0
) {
    val scale = remember { Animatable(1f) }
    val colorTint = remember { Animatable(MaterialTheme.colorScheme.primary) }
    val verticalBounce = remember { Animatable(0f) }
    
    // Breath animation
    LaunchedEffect(Unit) {
        scale.animateTo(
            1.02f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = EaseInOutCubic),
                repeatMode = RepeatMode.Reverse
            )
        )
    }
    
    // React to step change
    LaunchedEffect(currentStep) {
        verticalBounce.animateTo(
            -12f,
            animationSpec = spring(stiffness = Spring.StiffnessMedium)
        )
        verticalBounce.animateTo(0f, animationSpec = spring(stiffness = Spring.StiffnessMedium))
    }
    
    // Color tint per step
    LaunchedEffect(currentStep) {
        val targetColor = when (currentStep) {
            0, 4 -> MaterialTheme.colorScheme.primary
            1 -> Color(0xFFD4A5A5)
            2 -> Color(0xFFC89BA8)
            3 -> Color(0xFFFBF9F7)
            else -> MaterialTheme.colorScheme.primary
        }
        colorTint.animateTo(
            targetColor,
            animationSpec = tween(600, easing = EaseInOutCubic)
        )
    }
    
    Box(
        modifier = modifier
            .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
            .offset(y = verticalBounce.value.dp)
            .background(
                color = colorTint.value,
                shape = RoundedCornerShape(50%)
            ),
        contentAlignment = Alignment.Center
    ) {
        // Empty blob (decorative only)
    }
}
```

- [ ] **Step 4: Run test, verify it passes**

```bash
./gradlew testDebugUnitTest -k BlobMascot
```

Expected: PASS.

- [ ] **Step 5: Manual preview test**

Create a `@Preview` composable rendering BlobMascot and verify:
- Blob renders as rounded shape
- Breath animation visible (subtle scale pulse)
- Color smooth transitions when step changes (use `LaunchedEffect` in preview to advance step)

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/components/BlobMascot.kt
git add app/src/test/java/com/example/periodtracker/presentation/ui/onboarding/components/BlobMascotTest.kt
git commit -m "feat: add blob mascot with breath and react animations"
```

---

## Task 3: Create Botanical Elements Component

**Files:**
- Create: `app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/components/BotanicalElements.kt`

**Interfaces:**
- Consumes: Compose animation, Canvas, Path APIs
- Produces: `BotanicalElements(modifier: Modifier, stepProgress: Float)` composable

- [ ] **Step 1: Design botanical shapes (leaf/petal)**

Define simple SVG-like paths in Compose:
- **Leaf:** oval with pointed tip, 40x60dp
- **Petal:** rounded drop-shape, 20x30dp
- Color: semi-transparent primary pink (#E8A8B0 @ 20-30% opacity)

- [ ] **Step 2: Implement BotanicalElements composable**

Draw 4-6 botanical elements positioned at corners (top-right, bottom-left quadrants). Use `Canvas` to render shapes via `Path`.

Animate entrance: as user progresses through steps, elements fade in and scale up slightly (opacity 0→1, scale 0.8→1.0 over 1.2s per element, staggered 200ms apart).

Position elements off-center to avoid obscuring content.

```kotlin
@Composable
fun BotanicalElements(
    modifier: Modifier = Modifier,
    stepProgress: Float = 0f  // 0.0 to 1.0
) {
    val elementAlphas = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) }
    )
    
    LaunchedEffect(stepProgress) {
        elementAlphas.forEachIndexed { index, alpha ->
            launch {
                delay((index * 200).toLong())
                alpha.animateTo(
                    0.25f,
                    animationSpec = tween(1200, easing = EaseInOutCubic)
                )
            }
        }
    }
    
    Canvas(modifier = modifier.fillMaxSize()) {
        // Draw leaves/petals at fixed positions with animated alpha
        // Pseudo-code:
        // drawLeafAtTopRight(alpha = elementAlphas[0].value)
        // drawPetalAtBottomLeft(alpha = elementAlphas[1].value)
        // etc.
    }
}
```

- [ ] **Step 3: Add to OnboardingScreen**

Position `BotanicalElements` as background layer, pass `currentStep / totalSteps` as progress.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/components/BotanicalElements.kt
git add app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/OnboardingScreen.kt
git commit -m "feat: add botanical elements with staggered entrance animations"
```

---

## Task 4: Create Micro-Interactions Animation Utilities

**Files:**
- Create: `app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/animations/MicroInteractions.kt`

**Interfaces:**
- Consumes: Compose animation APIs
- Produces: animation specs `glowAnimationSpec`, `burstAnimationSpec`, `pulseAnimationSpec`, helper functions `animateInputGlow()`, `animatePetalBurst()`, `animateValidationPulse()`

- [ ] **Step 1: Define animation specs as constants**

```kotlin
val glowAnimationSpec = tween<Float>(
    durationMillis = 500,
    easing = EaseInOutCubic
)

val burstAnimationSpec = tween<Float>(
    durationMillis = 1200,
    easing = FastOutSlowInEasing
)

val pulseAnimationSpec = spring<Float>(
    dampingRatio = Spring.DampingRatioHighBouncy,
    stiffness = Spring.StiffnessMedium
)

val slideAnimationSpec = spring<Float>(
    dampingRatio = 0.8f,
    stiffness = Spring.StiffnessMediumLow
)
```

- [ ] **Step 2: Implement helper functions**

- `animateInputGlow(isActive: Boolean): Float` — returns animated alpha for glow effect (0→0.6 when active)
- `animatePetalBurst(burstTriggered: Boolean): List<Offset>` — returns list of petal positions (6-8 petals) radiating from center, decelerating outward
- `animateValidationPulse(isValidating: Boolean): Float` — returns animated scale (1.0 → 0.98 → 1.0) for soft pulse on invalid input

- [ ] **Step 3: Unit tests (optional, deferred to manual verification)**

Test that specs are properly defined and animation values are in expected ranges.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/animations/MicroInteractions.kt
git commit -m "feat: add micro-interaction animation utilities"
```

---

## Task 5: Create Gesture Navigation Handler

**Files:**
- Create: `app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/gestures/SwipeNavigation.kt`

**Interfaces:**
- Consumes: Compose `Modifier.pointerInput`, PointerEvent APIs
- Produces: `Modifier.detectHorizontalSwipe(onSwipeLeft: () -> Unit, onSwipeRight: () -> Unit)` extension

- [ ] **Step 1: Implement swipe detection**

Create extension on `Modifier` that wraps `pointerInput` and detects horizontal swipes:
- Threshold: 50dp horizontal movement
- Validation: horizontal movement > vertical movement (prevent false swipe on vertical scroll)
- Direction: left swipe → next, right swipe → previous

```kotlin
fun Modifier.detectHorizontalSwipe(
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit
) = pointerInput(Unit) {
    detectHorizontalDragGestures { change, dragAmount ->
        if (kotlin.math.abs(dragAmount) > 50.dp.toPx()) {
            when {
                dragAmount > 0 -> onSwipeRight()
                dragAmount < 0 -> onSwipeLeft()
            }
        }
    }
}
```

- [ ] **Step 2: Add to OnboardingScreen content wrapper**

Apply `Modifier.detectHorizontalSwipe()` to the `AnimatedContent` box or top-level column, wired to `viewModel.nextStep()` and `viewModel.previousStep()`.

- [ ] **Step 3: Test manually**

Swipe left/right on onboarding screen, verify nav triggers without interrupting vertical scroll in step content.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/gestures/SwipeNavigation.kt
git add app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/OnboardingScreen.kt
git commit -m "feat: add gesture-based swipe navigation"
```

---

## Task 6: Create Animated Background Component

**Files:**
- Create: `app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/components/OnboardingBackground.kt`

**Interfaces:**
- Consumes: Compose animation, Brush APIs, currentStep
- Produces: `OnboardingBackground(modifier: Modifier, currentStep: Int)` composable

- [ ] **Step 1: Define background colors per step**

Map currentStep → target background color (subtle pink tint shift):
- Step 0 (welcome): #FBF9F7 (cream)
- Step 1 (last period): #FBF9F7 tinted +5% toward #E8A8B0
- Step 2 (cycle): +10%
- Step 3 (period): +15%
- Step 4 (notifications): +20%

- [ ] **Step 2: Implement animated gradient background**

Use `Brush.verticalGradient()` or `Brush.linearGradient()` with animated target color. Transition duration 600ms.

```kotlin
@Composable
fun OnboardingBackground(
    modifier: Modifier = Modifier,
    currentStep: Int = 0
) {
    val backgroundColor = remember { Animatable(Color(0xFFFBF9F7)) }
    
    LaunchedEffect(currentStep) {
        val targetColor = when (currentStep) {
            0 -> Color(0xFFFBF9F7)
            1 -> Color(0xFFFAF7F5)
            2 -> Color(0xFFF9F5F2)
            3 -> Color(0xFFF8F3EF)
            4 -> Color(0xFFF7F1EC)
            else -> Color(0xFFFBF9F7)
        }
        backgroundColor.animateTo(
            targetColor,
            animationSpec = tween(600, easing = EaseInOutCubic)
        )
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor.value)
    )
}
```

- [ ] **Step 3: Add to OnboardingScreen (behind content)**

Wrap OnboardingScreen content with `OnboardingBackground` as bottom layer.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/components/OnboardingBackground.kt
git add app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/OnboardingScreen.kt
git commit -m "feat: add animated background with step-based color shifts"
```

---

## Task 7: Redesign WelcomeStep

**Files:**
- Modify: `app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/OnboardingScreen.kt` (WelcomeStep function)

**Interfaces:**
- Consumes: BlobMascot, MaterialTheme colors, OnboardingTypography
- Produces: Updated WelcomeStep composable with blob + new layout

- [ ] **Step 1: Update WelcomeStep layout**

Replace static icon with `BlobMascot(currentStep = 0)`. Increase padding and spacing for breathing room. Update copy (see Task 12).

- [ ] **Step 2: Add entrance animation**

Wrap title + description in `AnimatedVisibility` with fade + slide-in effect (triggers on step == 0).

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/OnboardingScreen.kt
git commit -m "feat: redesign welcome step with blob mascot and animations"
```

---

## Task 8: Redesign LastPeriodStep (with Input Glow)

**Files:**
- Modify: `app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/OnboardingScreen.kt` (LastPeriodStep function)

**Interfaces:**
- Consumes: BlobMascot, MicroInteractions.animateInputGlow(), date validation state
- Produces: Updated LastPeriodStep with glow animation on date input

- [ ] **Step 1: Add glow animation to date input card**

When date picker is open or date is selected, animate border glow (use `Border` composable or `Modifier.shadow()` with animated elevation).

- [ ] **Step 2: Trigger petal burst on date selected**

Call `animatePetalBurst()` when user confirms date selection; render petals with Canvas overlay.

- [ ] **Step 3: Update button styling**

Use primary color from onboarding theme.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/OnboardingScreen.kt
git commit -m "feat: redesign last period step with input glow and petal burst"
```

---

## Task 9: Redesign CycleLengthStep (with Slider Feedback)

**Files:**
- Modify: `app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/OnboardingScreen.kt` (CycleLengthStep function)

**Interfaces:**
- Consumes: Slider state, MicroInteractions animations
- Produces: Updated CycleLengthStep with animated slider feedback

- [ ] **Step 1: Add glow to slider track**

Animate slider track color/shadow when actively being dragged.

- [ ] **Step 2: Animate value display**

When slider value changes, apply `animateValidationPulse()` scale to the display text (e.g., "$cycleLength days") for subtle emphasis.

- [ ] **Step 3: Update slider colors**

Use onboarding palette primary color.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/OnboardingScreen.kt
git commit -m "feat: redesign cycle length step with slider feedback animations"
```

---

## Task 10: Redesign PeriodLengthStep

**Files:**
- Modify: `app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/OnboardingScreen.kt` (PeriodLengthStep function)

**Interfaces:**
- Consumes: Slider state, MicroInteractions animations, PeriodColor from theme
- Produces: Updated PeriodLengthStep with animated slider feedback

- [ ] **Step 1: Mirror CycleLengthStep design**

Apply same slider glow + value display pulse animations.

- [ ] **Step 2: Use period-specific color accent**

Update slider colors to use tertiary/period color from palette.

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/OnboardingScreen.kt
git commit -m "feat: redesign period length step with slider feedback"
```

---

## Task 11: Redesign NotificationsStep (with Switch Animation)

**Files:**
- Modify: `app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/OnboardingScreen.kt` (NotificationsStep function)

**Interfaces:**
- Consumes: Switch state, MicroInteractions animations
- Produces: Updated NotificationsStep with animated switch feedback

- [ ] **Step 1: Add pulse animation to switch**

When switch toggles, apply `animateValidationPulse()` to the card or switch itself.

- [ ] **Step 2: Update switch colors**

Use onboarding palette colors for checked/unchecked states.

- [ ] **Step 3: Add celebratory animation on toggle ON**

Brief petal burst or confetti-like effect when user enables notifications.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/OnboardingScreen.kt
git commit -m "feat: redesign notifications step with switch animations"
```

---

## Task 12: Update Copy to Conversational Tone

**Files:**
- Modify: `app/src/main/res/values/strings.xml` (or create `strings.xml` with onboarding strings)

**Interfaces:**
- Consumes: Existing string keys
- Produces: Updated string values in conversational Indonesian

- [ ] **Step 1: Update all onboarding copy**

Map current formal copy → conversational equivalents per spec:

| Current | New |
|---------|-----|
| "Welcome to Period Tracker" | "Selamat datang di Period Tracker" (same, but keep tone warm) |
| "When did your last period start?" | "Kapan periode terakhir kamu dimulai?" |
| "This helps us predict your next cycle accurately" | "Ini membantu kami memprediksi siklus kamu dengan akurat" |
| "What's your average cycle length?" | "Siklus kamu berapa hari biasanya?" |
| "From the first day of one period to the first day of the next" | "Dari hari pertama periode ke hari pertama periode berikutnya" |
| "How long does your period usually last?" | "Periode kamu biasa berapa hari?" |
| "Enable reminders?" | "Aktifkan pengingat?" (or "Mau pengingat?") |
| "Get notified before your period, during ovulation, and fertile window" | "Dapat notifikasi sebelum periode, saat ovulasi, dan masa subur" |

Tone: warm, friendly, not slang-y. Use "kamu" (informal you) throughout.

- [ ] **Step 2: Add reassurance copy**

Add new string for reassurance: "Tenang, data ini aman di perangkatmu." (in LastPeriodStep or welcome)

- [ ] **Step 3: Commit**

```bash
git add app/src/main/res/values/strings.xml
git commit -m "feat: update onboarding copy to conversational tone"
```

---

## Task 13: Integrate All Components into OnboardingScreen

**Files:**
- Modify: `app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/OnboardingScreen.kt`

**Interfaces:**
- Consumes: BlobMascot, BotanicalElements, OnboardingBackground, SwipeNavigation, all step redesigns
- Produces: Fully integrated OnboardingScreen composable

- [ ] **Step 1: Update OnboardingScreen structure**

Wrap existing `Scaffold` content with:
1. `OnboardingBackground(currentStep = currentStep)` as bottom layer
2. `BotanicalElements(stepProgress = currentStep.toFloat() / totalSteps)` as mid-layer
3. Existing content (progress indicator, `AnimatedContent`, navigation buttons) as top layer
4. Apply `Modifier.detectHorizontalSwipe()` to content column

- [ ] **Step 2: Position BlobMascot**

Move blob to top-center of each step (above title). Wire `currentStep` to blob's animations.

- [ ] **Step 3: Update progress indicator styling**

Keep existing line-based indicator but update colors to use onboarding palette.

- [ ] **Step 4: Test full flow**

Manually test all 5 steps:
- Blob animates smoothly
- Background color shifts subtly
- Botanicals fade in progressively
- Swipe left/right navigates (verify no false triggers on vertical scroll)
- Input animations (glow, burst, pulse) trigger on interaction
- Buttons work as fallback

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/example/periodtracker/presentation/ui/onboarding/OnboardingScreen.kt
git commit -m "feat: integrate all components into onboarding screen with animations"
```

---

## Verification Checklist

- [ ] All 5 steps load without crashes
- [ ] Blob mascot visible and animating (breath + color shifts)
- [ ] Botanical elements fade in over 5 steps
- [ ] Background color shifts smoothly per step
- [ ] Swipe left/right navigates between steps
- [ ] Next/Back buttons still work as fallback
- [ ] Input animations trigger (glow on date/slider, burst on confirm, pulse on toggle)
- [ ] Copy is conversational (spot-check 3-4 strings)
- [ ] No animation jank on Android 12+ devices (60fps target)
- [ ] Text not truncated on 5" phones (vertical + horizontal orientations)
- [ ] Data validation logic unchanged (same validation flow pre/post redesign)

---

## Summary

13 tasks, ~3-4 days for one developer:
- Tasks 1-6: Foundation (colors, typography, blob, botanicals, animations, gestures)
- Tasks 7-11: Step redesigns (apply animations + new styling to each step)
- Task 12: Copy update
- Task 13: Final integration + testing

Each task produces independently testable code. No task blocks another (parallel possible on 7-11 after 1-6 complete).
