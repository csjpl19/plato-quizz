/**
 * Quick Quiz Logic - French API v2 Stable
 */

const API_URL = "https://quizzapi.jomoreschi.fr/api/v2/quiz?limit=10";

// DOM Elements
const loader = document.getElementById('loader');
const questionCard = document.getElementById('question-card');
const resultCard = document.getElementById('result-card');
const questionText = document.getElementById('question-text');
const optionsContainer = document.getElementById('options-container');
const questionNumber = document.getElementById('question-number');
const timerDisplay = document.getElementById('quiz-timer');
const progressFill = document.getElementById('progress-fill');
const scoreDisplay = document.getElementById('current-score');
const finalScoreDisplay = document.getElementById('final-score');
const correctCountDisplay = document.getElementById('correct-count');
const categoryTag = document.getElementById('question-category');

// Game State
let questions = [];
let currentQuestionIndex = 0;
let score = 0;
let correctAnswers = 0;
let timer;
let timeLeft = 15;
let acceptingAnswers = false;

/**
 * Initialize Quiz
 */
async function startQuiz() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error("Réponse API non valide");
        
        const data = await response.json();
        
        // Structure v2: { quizzes: [...] }
        if (data.quizzes && data.quizzes.length > 0) {
            questions = data.quizzes;
            loader.hidden = true;
            questionCard.hidden = false;
            showQuestion();
        } else {
            throw new Error("Aucune question trouvée dans la réponse");
        }
    } catch (error) {
        console.error("Erreur API:", error);
        loader.innerHTML = `
            <p style="color:var(--error); margin-bottom:1rem;">Verifier votre connexion d'internet.</p>
            <button onclick="location.reload()" class="btn btn-secondary">Réessayer</button>
        `;
    }
}

/**
 * Display current question
 */
function showQuestion() {
    resetState();
    const q = questions[currentQuestionIndex];
    
    // UI Update
    questionNumber.innerText = `Question ${currentQuestionIndex + 1}/${questions.length}`;
    categoryTag.innerText = formatCategory(q.category);
    questionText.innerText = q.question;
    progressFill.style.width = `${((currentQuestionIndex + 1) / questions.length) * 100}%`;

    // Mix answer + badAnswers (v2 structure)
    const answers = [...q.badAnswers, q.answer];
    shuffleArray(answers);

    // Create buttons
    answers.forEach(answer => {
        const button = document.createElement('button');
        button.innerText = answer;
        button.classList.add('option-btn');
        button.addEventListener('click', () => selectAnswer(button, answer === q.answer));
        optionsContainer.appendChild(button);
    });

    startTimer();
    acceptingAnswers = true;
}

/**
 * Handle answer selection
 */
function selectAnswer(selectedBtn, isCorrect) {
    if (!acceptingAnswers) return;
    acceptingAnswers = false;
    clearInterval(timer);

    const q = questions[currentQuestionIndex];
    
    if (isCorrect) {
        selectedBtn.classList.add('correct');
        score += (timeLeft * 10) + 100;
        correctAnswers++;
        scoreDisplay.innerText = score;
    } else {
        selectedBtn.classList.add('wrong');
        // Show correct answer
        Array.from(optionsContainer.children).forEach(btn => {
            if (btn.innerText === q.answer) {
                btn.classList.add('correct');
            }
        });
    }

    setTimeout(() => {
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.length) {
            showQuestion();
        } else {
            finishQuiz();
        }
    }, 1500);
}

/**
 * Timer logic
 */
function startTimer() {
    timeLeft = 15;
    timerDisplay.innerText = `${timeLeft}s`;
    timerDisplay.style.color = 'var(--primary)';

    timer = setInterval(() => {
        timeLeft--;
        timerDisplay.innerText = `${timeLeft}s`;

        if (timeLeft <= 5) timerDisplay.style.color = 'var(--error)';

        if (timeLeft <= 0) {
            clearInterval(timer);
            autoSubmit();
        }
    }, 1000);
}

function autoSubmit() {
    acceptingAnswers = false;
    const q = questions[currentQuestionIndex];
    Array.from(optionsContainer.children).forEach(btn => {
        if (btn.innerText === q.answer) btn.classList.add('correct');
    });

    setTimeout(() => {
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.length) showQuestion();
        else finishQuiz();
    }, 1500);
}

function finishQuiz() {
    questionCard.hidden = true;
    resultCard.hidden = false;
    finalScoreDisplay.innerText = score;
    correctCountDisplay.innerText = `${correctAnswers}/${questions.length}`;
}

function resetState() {
    clearInterval(timer);
    while (optionsContainer.firstChild) {
        optionsContainer.removeChild(optionsContainer.firstChild);
    }
}

function formatCategory(cat) {
    if (!cat) return "Culture Générale";
    return cat.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase());
}

function shuffleArray(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
}

startQuiz();
