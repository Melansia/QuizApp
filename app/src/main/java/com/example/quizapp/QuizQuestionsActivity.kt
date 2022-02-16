package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.AdaptiveIconDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUserName: String? = null

    private lateinit var tvQuestion: TextView
    private lateinit var tvAnswerOne: TextView
    private lateinit var tvAnswerTwo: TextView
    private lateinit var tvAnswerThree: TextView
    private lateinit var tvAnswerFour: TextView

    private lateinit var ivImage: ImageView

    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgress: TextView

    private lateinit var btnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)
//      var tvAnswerOne : TextView = findViewById(R.id.tvAnswerOne)

        tvQuestion = findViewById(R.id.tvQuestion)
        tvAnswerOne = findViewById(R.id.tvAnswerOne)
        tvAnswerTwo = findViewById(R.id.tvAnswerTwo)
        tvAnswerThree = findViewById(R.id.tvAnswerThree)
        tvAnswerFour = findViewById(R.id.tvAnswerFour)

        ivImage = findViewById(R.id.ivImage)

        progressBar = findViewById(R.id.progressBar)
        tvProgress = findViewById(R.id.tvProgress)

        btnSubmit = findViewById(R.id.btnSubmit)



        mQuestionsList = Constants.getQuestions()

        setQuestion()

        tvAnswerOne.setOnClickListener(this)
        tvAnswerTwo.setOnClickListener(this)
        tvAnswerThree.setOnClickListener(this)
        tvAnswerFour.setOnClickListener(this)

        btnSubmit.setOnClickListener(this)

    }

    private fun setQuestion() {

//        mCurrentPosition = 1
        val question = mQuestionsList!![mCurrentPosition - 1]
        // Each time we set a new question we set the
        // Setting the default options just to make sure that our buttons are set to the default appearance
        defaultOptionsView()

        if (mCurrentPosition == mQuestionsList!!.size) {
            btnSubmit.text = "FINISH"
        } else {
            btnSubmit.text = "SUBMIT"
        }

        tvQuestion.text = question!!.question
        ivImage.setImageResource(question.image)

        progressBar.progress = mCurrentPosition
        tvProgress.text = "$mCurrentPosition" + "/" + progressBar.max

        tvAnswerOne.text = question.optionOne
        tvAnswerTwo.text = question.optionTwo
        tvAnswerThree.text = question.optionThree
        tvAnswerFour.text = question.optionFour

    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(0, tvAnswerOne)
        options.add(1, tvAnswerTwo)
        options.add(2, tvAnswerThree)
        options.add(3, tvAnswerFour)

        // A four loop for all the Answers so it will set the colour around the answer
        for (option in options) {

            // Setting the desirable color for the questions
            option.setTextColor(Color.parseColor("#7A8089"))
            // type face can be BOLD , DEFAULT etc
            option.typeface = Typeface.DEFAULT
            // Assigning a background for Answers ( in this case is the border)
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvAnswerOne -> {
                selectedOptionView(tvAnswerOne, 1)
            }
            R.id.tvAnswerTwo -> {
                selectedOptionView(tvAnswerTwo, 2)
            }
            R.id.tvAnswerThree -> {
                selectedOptionView(tvAnswerThree, 3)
            }
            R.id.tvAnswerFour -> {
                selectedOptionView(tvAnswerFour, 4)
            }
            R.id.btnSubmit -> {
                if (mSelectedOptionPosition == 0) {
                    mCurrentPosition++

                    when {
                        mCurrentPosition <= mQuestionsList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    val question = mQuestionsList?.get(mCurrentPosition - 1)
                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    } else {
                        mCorrectAnswers++
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    if (mCurrentPosition == mQuestionsList!!.size) {
                        btnSubmit.text = "FINISH"
                    } else {
                        btnSubmit.text = "GO TO NEXT QUESTION "
                    }
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                tvAnswerOne.background = ContextCompat.getDrawable(this, drawableView)
            }
            2 -> {
                tvAnswerTwo.background = ContextCompat.getDrawable(this, drawableView)
            }
            3 -> {
                tvAnswerThree.background = ContextCompat.getDrawable(this, drawableView)
            }
            4 -> {
                tvAnswerFour.background = ContextCompat.getDrawable(this, drawableView)
            }
        }

    }

    private fun selectedOptionView(
        tv: TextView,
        selectedOptionNum: Int
    ) {
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(Color.parseColor("#7A8089"))
        // Setting the style when user click
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        // Assigning a background for Answers ( in this case is the border)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }
}