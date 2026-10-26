# Design System: Cycle — Expressive Period Tracker

## 1. Visual Theme & Atmosphere
A confident, asymmetric interface with fluid spring-physics motion and restrained color use. The atmosphere is clinical yet warm — like a well-designed wellness studio. High variance in layout (offset grids, left-aligned heroes), medium density with generous breathing room, and perpetual micro-motion on interactive elements. Sophistication through restraint, not decoration.

**Density:** 5 (Daily App Balanced)  
**Variance:** 8 (Offset Asymmetric)  
**Motion:** 7 (Choreographed Spring Physics)

## 2. Color Palette & Roles

**Neutrals (Primary Surfaces):**
- **Canvas Warm** (#FAFAF8) — Primary background, off-white with warm undertone
- **Pure Surface** (#FFFFFF) — Card and container fill
- **Charcoal Ink** (#1C1917) — Primary text, Stone-950 depth
- **Muted Slate** (#78716C) — Secondary text, descriptions, metadata (Stone-500)
- **Whisper Border** (rgba(231,229,228,0.4)) — Card borders, 1px structural lines (Stone-200)

**Accent (Single Color Rule):**
- **Terracotta Clay** (#DC6B4A) — Primary CTAs, active states, focus rings, period markers (Saturation: 62%)

**Semantic Status Colors:**
- **Fertile Sage** (#6B8E76) — Fertile window indicators (Saturation: 25%)
- **Ovulation Amber** (#D4A574) — Ovulation day markers (Saturation: 47%)
- **PMS Soft Lilac** (#B5A4C1) — PMS phase markers (Saturation: 24%)

**Banned:**
- No pink/magenta (#EC407A, #F48FB1) — stereotypical period app cliché
- No pure black (#000000)
- No neon or outer glows
- No purple/blue neon gradients

## 3. Typography Rules

**Display/Headlines:** Outfit  
- Track-tight (-0.02em to -0.04em), controlled scale
- Light weight (300) for large displays, Medium (500) for sub-headlines
- Hierarchy through weight and color, not excessive size

**Body/UI Text:** Outfit  
- Relaxed leading (1.6 line-height), 65ch max-width for paragraphs
- Regular weight (400) for body, Medium (500) for labels/buttons
- Secondary text uses Muted Slate color

**Monospace (Metadata/Numbers):** JetBrains Mono  
- For cycle day counts, date timestamps, numeric metrics
- Regular weight (400), slightly reduced scale (0.95em)

**Scale Hierarchy:**
- Display Large: 32sp, Light (300), -0.64sp tracking
- Headline Large: 22sp, Medium (500), 0sp tracking
- Body Large: 16sp, Regular (400), 0.15sp tracking
- Label Large: 14sp, Medium (500), 0.1sp tracking

**Banned:**
- Inter font (overused, no character)
- Generic serif fonts (Times New Roman, Georgia, Garamond)
- System fonts in premium contexts

## 4. Component Stylings

### Buttons
- **Primary:** Terracotta Clay fill, white text, 12px rounded corners
- **Interaction:** Tactile -1px translateY on active state, 200ms spring
- **No outer glow shadows**
- **Ghost variant:** Terracotta Clay border (1px), transparent fill

### Cards
- **Shape:** 20px rounded corners (generous but not excessive)
- **Shadow:** Diffused 0 2px 8px rgba(28,25,23,0.06) — tinted to Charcoal Ink
- **Background:** Pure Surface (#FFFFFF)
- **Border:** 1px Whisper Border when elevation not needed
- **Usage:** Only when elevation communicates hierarchy. For lists, use border-top dividers instead

### Inputs/Forms
- **Label:** Above input, Muted Slate color, Label Medium style
- **Field:** 12px rounded, 1px Whisper Border, Pure Surface fill
- **Focus:** 2px Terracotta Clay ring, no outer glow
- **Error:** Below field, red-tinted text, inline icon

### Calendar Day Cells
- **Default:** 40x40px square, 8px rounded
- **Today:** 2px Terracotta Clay border
- **Period:** Terracotta Clay fill, white text
- **Fertile:** Fertile Sage tinted background (10% opacity), Sage border
- **Ovulation:** Ovulation Amber tinted background (10% opacity), Amber border
- **PMS:** PMS Soft Lilac tinted background (10% opacity), Lilac border
- **Interaction:** Scale(0.95) on press, spring back

### Loading States
- **Skeletal shimmer** matching exact layout dimensions
- No generic circular spinners
- Shimmer animation: translateX infinite loop, 1.5s duration

### Empty States
- Illustration + text composition
- Muted Slate text, centered vertically in container
- Icon above text (64px), not just plain text

## 5. Layout Principles

**Asymmetric Structure:**
- Hero sections: Left-aligned, offset text blocks with right-side visual breathing room
- No centered hero layouts — force Split Screen or Left-Aligned compositions
- Calendar grid: Full-width on mobile, max-width 400px centered on tablet/desktop

**Grid System:**
- CSS Grid over Flexbox math
- 24px horizontal padding on mobile, 32px on tablet+
- Vertical section spacing: clamp(48px, 8vw, 96px)
- Max-width containment: 1400px centered for ultra-wide screens

**Spacing Philosophy:**
- Generous internal padding (24dp cards)
- Consistent 16px gaps between related elements
- 32px gaps between sections
- No overlapping elements — clean spatial separation

**Mobile-First Collapse:**
- All multi-column layouts collapse to single column < 768px
- No horizontal scroll on mobile (critical failure if present)
- Touch targets minimum 44px

## 6. Motion & Interaction

**Spring Physics (Default):**
```
stiffness: 100
damping: 20
mass: 1
```
Premium, weighty feel. No linear easing.

**Perpetual Micro-Interactions:**
- Calendar today indicator: Pulse animation (scale 1.0 → 1.05 → 1.0, 2s infinite)
- Period prediction shimmer: Gradient shift loop on future period cells
- Onboarding progress dots: Scale animation on current step

**Staggered Orchestration:**
- Calendar days: Cascade reveal with 30ms delay per cell
- List items: Waterfall mount with 50ms stagger
- Cards: Fade + translateY(8px) on mount

**Performance Rules:**
- Animate exclusively via `transform` and `opacity`
- Never animate `top`, `left`, `width`, `height`
- Hardware-accelerated transforms only
- Grain/noise filters on fixed pseudo-elements only

## 7. Responsive Rules

**Breakpoints:**
- Mobile: < 768px (single column)
- Tablet: 768px - 1024px
- Desktop: > 1024px

**Typography Scaling:**
- Headlines: clamp(24px, 5vw, 32px)
- Body: minimum 14px, never scale below

**Image Behavior:**
- Onboarding icons scale proportionally: 64px → 48px on mobile
- Calendar cells: 40x40px → 36x36px on small screens

**Navigation:**
- Bottom navigation on mobile (fixed)
- Side navigation on desktop (persistent)

**Spacing:**
- Vertical gaps: clamp(3rem, 8vw, 6rem)
- Horizontal padding: clamp(24px, 4vw, 32px)

## 8. Anti-Patterns (Banned)

**Color:**
- ❌ Generic pink/magenta (#EC407A, #FF4081) — period app stereotype
- ❌ Pure black (#000000)
- ❌ Neon/outer glow shadows
- ❌ Oversaturated accents (> 80% saturation)
- ❌ Purple/blue neon gradients

**Typography:**
- ❌ Inter font
- ❌ Generic serif fonts (Times New Roman, Georgia, Garamond, Palatino)
- ❌ System fonts in premium contexts

**Layout:**
- ❌ Centered Hero sections (variance > 4)
- ❌ 3-column equal card layouts
- ❌ Overlapping elements (text over images)
- ❌ Horizontal scroll on mobile

**Content:**
- ❌ Emojis anywhere
- ❌ AI copywriting clichés ("Elevate", "Seamless", "Unleash", "Next-Gen")
- ❌ Filler UI text ("Scroll to explore", "Swipe down", scroll arrows)
- ❌ Generic placeholder names ("Jane Doe", "Acme Corp")
- ❌ Fake metrics (99.99%, fabricated statistics)
- ❌ Broken Unsplash links
- ❌ `LABEL // YEAR` formatting

**Components:**
- ❌ Custom mouse cursors
- ❌ Generic circular loading spinners
- ❌ Excessive gradient text on large headers

## 9. Implementation Notes

**Compose Material3 Migration:**
- Replace `FontFamily.Default` with Outfit (requires font import)
- Update Color.kt: remove all Pink* tokens, replace with new palette
- Update Theme.kt: rebuild lightColorScheme/darkColorScheme
- Refactor OnboardingScreen: asymmetric layouts, offset text blocks
- Add spring animation specs globally

**Color Token Mapping:**
```kotlin
// Old → New
PinkPrimary → TerracottaClay
PinkSurface → CanvasWarm
PinkOnSurface → CharcoalInk
PinkOnSurfaceVariant → MutedSlate
PeriodColor → TerracottaClay
FertileColor → FertileSage
OvulationColor → OvulationAmber
PmsColor → PmsSoftLilac
```

**Font Integration:**
```kotlin
// Download Outfit, JetBrains Mono
// Add to res/font/
val OutfitFontFamily = FontFamily(
    Font(R.font.outfit_light, FontWeight.Light),
    Font(R.font.outfit_regular, FontWeight.Normal),
    Font(R.font.outfit_medium, FontWeight.Medium)
)
```

---

**Philosophy:** Sophistication through restraint. Premium health tracking without stereotypical gendered aesthetics. Confident, asymmetric, fluid.
