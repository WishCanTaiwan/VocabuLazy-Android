package wishcantw.vocabulazy.exam.model;

import android.content.Context;
import android.support.annotation.NonNull;

import wishcantw.vocabulazy.database.Database;
import wishcantw.vocabulazy.database.DatabaseUtils;
import wishcantw.vocabulazy.database.object.Vocabulary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by swallow on 2016/1/17.
 */
public class ExamModel {

    public static final String TAG = ExamModel.class.getSimpleName();

    // singleton
    private static ExamModel examModel = new ExamModel();

    // private constructor
    private ExamModel() {}

    // getter of singleton
    public static ExamModel getInstance() {
        return examModel;
    }

    private Database database;
    private DatabaseUtils databaseUtils;

    public void init() {
        if (database == null) {
            database = Database.getInstance();
        }

        if (databaseUtils == null) {
            databaseUtils = DatabaseUtils.getInstance();
        }
    }

    public String getTitle(@NonNull Context context,
                           int bookIndex,
                           int lessonIndex) {
        return (bookIndex == -1)
                ? databaseUtils.getNoteTitle(context, database.getNotes(), lessonIndex)
                : databaseUtils.getLessonTitle(context, database.getTextbooks(), bookIndex, lessonIndex);
    }

    public int getContentAmount(int bookIndex,
                                int lessonIndex) {
        return (bookIndex == -1)
                ? databaseUtils.getNoteContent(database.getNotes(), lessonIndex).size()
                : databaseUtils.getLessonContent(database.getTextbooks(), bookIndex, lessonIndex).size();
    }

    public PuzzleSetter createPuzzleSetter(int bookIndex, int lessonIndex) {
        ArrayList<Vocabulary> vocabularies = database.getVocabularies();
        return (bookIndex == -1)
                ? new PuzzleSetter(databaseUtils.getVocabulariesByIDs(vocabularies, databaseUtils.getNoteContent(database.getNotes(), lessonIndex)))
                : new PuzzleSetter(databaseUtils.getVocabulariesByIDs(vocabularies, databaseUtils.getLessonContent(database.getTextbooks(), bookIndex, lessonIndex)));
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

        /**
         *
         * @return HashMap<Integer, ArrayList<String>>
         *     KEY is QuestionAndOption, 0 means Question, 1-4 means option
         *     VALUE is english and translation
         */
        public HashMap<Integer, ArrayList<String>> getANewQuestion(){
            // Because one of option is correct answer,
            // it is in the condition of (pickIndex == mCurrentQuestionIndex)
            int pickedAnswerIndexArray[] = {-1, -1, -1};
            // pt is used for recording how may pickedAnswerIndex is recorded.
            int pt = 0;

            // Null map means not enough question to start exam
            if(mTotalQuestionNum < 5) {
                return null;
            }

            mCurrentQuestionIndex++;

            // If current question index >= total question number, the exam is over. Return empty map
            HashMap<Integer, ArrayList<String>> questionMap = new HashMap<>();
            if (mCurrentQuestionIndex >= mTotalQuestionNum) {
                return questionMap;
            }

            mAnswerOptionIndex = 0;     // reset the answer_option index
            do{     // Determine which option will be the answer
                mAnswerOptionIndex = new Random().nextInt(5);
            } while(mAnswerOptionIndex == 0);

            for(int i = 0; i < 5; i++){
                int pickIndex;      // It's used to pick up option from vocabularies
                ArrayList<String> strArr = new ArrayList<>();   //save option and answer string
                // 0 is for question, only spell is needed
                if(i == 0) {
                    pickIndex = mCurrentQuestionIndex;
                    strArr.add(mVocabularies.get(pickIndex).getSpell());
                }
                // if (i == mAnswerOptionIndex) means the option is the answer
                else if(i == mAnswerOptionIndex){
                    pickIndex = mCurrentQuestionIndex;
                    strArr.add("");
                    strArr.add(mVocabularies.get(pickIndex).getTranslation());
                }
                // otherwise, all others are wrong answers. Record it to prevent duplicate options
                else {
                    do {
                        pickIndex = new Random().nextInt(mTotalQuestionNum);
                    } while(pickIndex == mCurrentQuestionIndex
                            || pickIndex == pickedAnswerIndexArray[0]
                            || pickIndex == pickedAnswerIndexArray[1]
                            || pickIndex == pickedAnswerIndexArray[2]);
                    strArr.add(mVocabularies.get(pickIndex).getSpell());
                    strArr.add(mVocabularies.get(pickIndex).getTranslation());
                    pickedAnswerIndexArray[pt++] = pickIndex;
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
