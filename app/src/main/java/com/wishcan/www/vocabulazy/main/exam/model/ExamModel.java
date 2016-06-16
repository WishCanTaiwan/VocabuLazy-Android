package com.wishcan.www.vocabulazy.main.exam.model;

import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.main.voc.model.VocModel;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Book;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Lesson;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by swallow on 2016/1/17.
 */
public class ExamModel {

    public static final String TAG = ExamModel.class.getSimpleName();

    private Database mDatabase;

    public ExamModel(VLApplication vlApplication){
        mDatabase = vlApplication.getDatabase();
    }

    public String getBookTitle(int bookIndex) {
        return mDatabase.getBookTitle(bookIndex);
    }

    public String getLessonTitle(int bookIndex, int lessonIndex) {
        return mDatabase.getLessonTitle(bookIndex, lessonIndex);
    }

    public ArrayList<Book> getBooks() {
        return mDatabase.getBooks();
    }

    public ArrayList<Lesson> getLessons(int bookIndex) {
        return mDatabase.getLessonsByBook(bookIndex);
    }

    public ArrayList<Integer> getContent(int bookIndex, int lessonIndex) {
        return mDatabase.getContentIDs(bookIndex, lessonIndex);
    }

    public PuzzleSetter createPuzzleSetter(int bookIndex, int lessonIndex) {
        ArrayList<Vocabulary> vocabularies = mDatabase.getVocabulariesByIDs(mDatabase.getContentIDs(bookIndex, lessonIndex));
        return new PuzzleSetter(vocabularies);
    }

    public class PuzzleSetter {

        private int mCurrentQuestionIndex;
        private int mTotalQuestionNum;
        private int mAnswerOptionIndex;
        private int mCorrectCount;

        private ArrayList<Vocabulary> mVocabularies;

        public PuzzleSetter(ArrayList<Vocabulary> vocabularies) {
            mVocabularies = shuffleArrayList(vocabularies);
            mCurrentQuestionIndex = -1;
            mTotalQuestionNum = mVocabularies.size();
            mCorrectCount = 0;
        }

        public HashMap<Integer, ArrayList<String>> getANewQuestion(){
            /***
             * HashMap<Integer, ArrayList<String>>
             *     KEY is QuestionAndOption, 0 means Question, 1-4 means option
             *     VALUE is english and translation
             */

            if(mTotalQuestionNum < 5) {
                Log.d("ExamFragment", "mVocabularies = " + mTotalQuestionNum);
                return null;
            }


            mCurrentQuestionIndex++;

            HashMap<Integer, ArrayList<String>> questionMap = new HashMap<>();

            mAnswerOptionIndex = 0;     // reset the answer_option index
            do{     // Determine which option will be the answer
                mAnswerOptionIndex = new Random().nextInt(5);
            } while(mAnswerOptionIndex == 0);

            for(int i = 0; i < 5; i++){
                int pickIndex;      // It's used to pick up option from vocabularies
                ArrayList<String> strArr = new ArrayList<>();   //save option and answer string
                if(i == 0) {          // 0 is for question, only spell is needed
                    pickIndex = mCurrentQuestionIndex;
                    strArr.add(mVocabularies.get(pickIndex).getSpell());
                }
                else if(i == mAnswerOptionIndex){
                    pickIndex = mCurrentQuestionIndex;
                    strArr.add("");
                    strArr.add(mVocabularies.get(pickIndex).getTranslation());
                }
                else {
                    do {
                        pickIndex = new Random().nextInt(mTotalQuestionNum);
                    } while(pickIndex == mCurrentQuestionIndex);
                    strArr.add(mVocabularies.get(pickIndex).getSpell());
                    strArr.add(mVocabularies.get(pickIndex).getTranslation());
                }
                questionMap.put(i, strArr);
            }

            return questionMap;
        }

        public int checkAnswer(int checkIndex){
            if(checkIndex == mAnswerOptionIndex)
                mCorrectCount++;
            return mAnswerOptionIndex;
        }

        private ArrayList<Vocabulary> shuffleArrayList(ArrayList<Vocabulary> oldArrayList){
            ArrayList<Vocabulary> newArrayList = new ArrayList<>();
            Random rnd = new Random();
            for(int i = oldArrayList.size() - 1; i >= 0; i--) {
                int index = rnd.nextInt(i + 1);
                newArrayList.add(oldArrayList.get(index));
                oldArrayList.remove(index);
            }
            return newArrayList;
        }

        public int getCurrentQuestionIndex(){
            return mCurrentQuestionIndex + 1;       // because we can't show 00 to students
        }

        public int getTotalQuestionNum(){
            return mTotalQuestionNum;
        }

        public int getCorrectCount() { return mCorrectCount; }
    }


}
