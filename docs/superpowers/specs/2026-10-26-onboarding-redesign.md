# Onboarding Redesign: Playful Botanist

**Date:** 2026-10-26  
**Status:** Design Review

## Intent & Why

Current onboarding is formal, AI-generated looking, generic Material Design. Goal: warm, playful, feminim aesthetic that feels human-designed. Users entering sensitive health data should feel comfortable and delighted, not clinical.

## Goals

- Visual refresh: warm pink palette, soft typography, playful micro-interactions
- Feel: intimate, non-corporate, like talking to a friend
- Retain functional flow: 5 steps same sequence, data requirements unchanged
- Delightful interactions without sacrificing clarity

## Non-Goals

- Full character system or narrative (too much, risks over-design)
- Replace entire component library
- Change data validation logic
- Gesture-only navigation (accessibility concern)

## Anti-Goals

- Corporate, sterile, or over-designed aesthetic
- Slow/janky animations
- Copy that feels forced or cutesy-to-the-point-of-cringe

## Constraints

- Material 3 theming must adapt, not replace
- Compose-based, existing component compatibility
- Target Android 12+ (animation capabilities)
- Performance: micro-animations must not stutter (60fps target)

## Visual System

### Color Palette

| Role | Hex | Usage |
|------|-----|-------|
| Primary Pink | #E8A8B0 | Active states, accents, buttons |
| Dusty Rose | #D4A5A5 | Secondary elements, inactive text |
| Muted Mauve | #C89BA8 | Tertiary accents, borders |
| Cream | #FBF9F7 | Background |
| Dark text | #3D2E2B | On-background, readability |

### Typography

- **Headers (Headline/Display):** Rounded serif family (e.g., Crimson Text rounded, or Outfit + letter-spacing tweak for warmth)
- **Body/Labels:** Rounded sans (Comfortaa, Raleway, or rounded Inter variant)
- Retain Material 3 hierarchy but increase line-height (breathing room)

### Botanical Elements

- Leaf/petal SVG illustrations in corner regions (top-right, bottom-left)
- Grow/fade in per step completion (subtle opacity/scale animation)
- Non-intrusive: under 15% of screen real estate
- Color: semi-transparent pink (#E8A8B0 @ 20-30% opacity)

### Blob Mascot

- Abstract shape: rounded rectangle with 4 corners bulging (not character, not object—liminal mascot)
- Positioned top-center above title
- Subtle animations:
  - **Breath:** slow scale pulse (0.98 → 1.02, 2s loop)
  - **React:** small vertical bounce (8-12dp) when input validated
  - **Shift:** color tint per step (pink → rose → mauve → cream → pink)
- Size: ~80-100dp
- Accessibility: decorative, aria-hidden

## Interactions & Animations

### Navigation

- **Primary:** swipe left/right to navigate (GestureDetector)
- **Fallback:** visible Next/Back buttons (bottom row)
- Hybrid approach: both work, gesture preferred but optional
- Swipe threshold: 50dp horizontal movement
- Animation: spring-based exit/enter (stiffness=MediumLow, dampingRatio=0.8)

### Input Feedback

- **Slider/Date/Text fields:**
  - Active state: border glow animation (0.5s transition, alpha 0→0.6)
  - On valid input: mini burst (3-4 petals expand outward, fade in 300ms, fade out 400ms)
  - Color: primary pink with opacity gradient
- **Validation:** no error shake, instead soft pulse (scale 1.0 → 0.98 → 1.0, 400ms)

### Step Transitions

- Background subtle color shift (gradient toward step color, 0.6s easing)
- Content slide + fade (horizontal direction based on nav, 500ms spring)
- Progress indicator: animated line extend + dot activate

### Completion Celebration

- On final step valid + submit:
  - Background fills with semi-transparent pink (0.2 opacity, 600ms)
  - Petals burst from center (6-8 petals, radial scatter, 1.2s decelerate)
  - Text fades, fade in "Getting ready..." loading state

## Copy Tone

Conversational, warm, occasionally playful:

- "Kapan periode terakhir kamu?" (not "When did your last period start?")
- "Siklus kamu berapa hari biasanya?" (not "average cycle length")
- "Tenang, data ini aman di perangkatmu." (reassurance)
- "Periode biasa berapa hari?" (casual, not "duration")

Avoid: emoji spam, forced slang, infantilizing language.

## What's Left to Builder

- Exact rounded serif font selection (Crimson Text? Outfit? or custom Compose text style)
- Botanical SVG design (final shapes, stroke weight, animation easing curves)
- Blob shape refinement (exact bulge proportions, transform animations)
- Exact spring animation parameters (stiffness, damping—will tune for feel)
- Copy finalization & localization (Indonesian + future languages)
- Petals burst pattern (radial scatter math, final count, decay curves)

## Approach Rationale

This hybrid combines:
1. **Playful character** (blob) from Arah 1, but minimal to avoid over-design
2. **Gesture navigation + typography** from Arah 2, adding feminim warmth
3. **Animated data entry** from Arah 3, with celebratory micro-interactions

Kept: exact same 5 steps, data flow, validation logic. Only visual layer changes. Allows staged rollout and easy revert if needed.
