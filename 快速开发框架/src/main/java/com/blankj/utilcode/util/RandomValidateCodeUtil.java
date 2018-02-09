package com.blankj.utilcode.util;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;


/**
 * 随机数,验证码专用类
 * <ul>
 * Shuffling algorithm
 * <li>{@link #shuffle(Object[])} Shuffling algorithm, Randomly permutes the specified array using a default source of
 * randomness</li>
 * <li>{@link #shuffle(Object[], int)} Shuffling algorithm, Randomly permutes the specified array</li>
 * <li>{@link #shuffle(int[])} Shuffling algorithm, Randomly permutes the specified int array using a default source of
 * randomness</li>
 * <li>{@link #shuffle(int[], int)} Shuffling algorithm, Randomly permutes the specified int array</li>
 * </ul>
 * <ul>
 * get random int
 * <li>{@link #getRandom(int)} get random int between 0 and max</li>
 * <li>{@link #getRandom(int, int)} get random int between min and max</li>
 * </ul>
 * <ul>
 * get random numbers or letters
 * <li>{@link #getRandomCapitalLetters(int)} get a fixed-length random string, its a mixture of uppercase letters</li>
 * <li>{@link #getRandomLetters(int)} get a fixed-length random string, its a mixture of uppercase and lowercase letters
 * </li>
 * <li>{@link #getRandomLowerCaseLetters(int)} get a fixed-length random string, its a mixture of lowercase letters</li>
 * <li>{@link #getRandomNumbers(int)} get a fixed-length random string, its a mixture of numbers</li>
 * <li>{@link #getRandomNumbersAndLetters(int)} get a fixed-length random string, its a mixture of uppercase, lowercase
 * letters and numbers</li>
 * <li>{@link #getRandom(String, int)} get a fixed-length random string, its a mixture of chars in source</li>
 * <li>{@link #getRandom(char[], int)} get a fixed-length random string, its a mixture of chars in sourceChar</li>
 * </ul>
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-5-12
 */
public class RandomValidateCodeUtil {

    public static final String NUMBERS_AND_LETTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBERS             = "0123456789";
    public static final String LETTERS             = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String CAPITAL_LETTERS     = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER_CASE_LETTERS  = "abcdefghijklmnopqrstuvwxyz";

    private RandomValidateCodeUtil() {
        throw new AssertionError();
    }

    /**
     * 得到一个固定长度的随机字符串(来源:大写,小写字母和数字)
     *
     * @param length
     * @return
     * @see RandomValidateCodeUtil#getRandom(String source, int length)
     */
    public static String getRandomNumbersAndLetters(int length) {
        return getRandom(NUMBERS_AND_LETTERS, length);
    }

    /**
     * 得到一个固定长度的随机字符串(来源:数字)
     *
     * @param length
     * @return
     * @see RandomValidateCodeUtil#getRandom(String source, int length)
     */
    public static String getRandomNumbers(int length) {
        return getRandom(NUMBERS, length);
    }

    /**
     * 得到一个固定长度的随机字符串(来源:大写,小写字母)
     *
     * @param length
     * @return
     * @see RandomValidateCodeUtil#getRandom(String source, int length)
     */
    public static String getRandomLetters(int length) {
        return getRandom(LETTERS, length);
    }

    /**
     * 得到一个固定长度的随机字符串(来源:大写字母)
     *
     * @param length
     * @return
     * @see RandomValidateCodeUtil#getRandom(String source, int length)
     */
    public static String getRandomCapitalLetters(int length) {
        return getRandom(CAPITAL_LETTERS, length);
    }

    /**
     * 得到一个固定长度的随机字符串(来源:小写字母)
     *
     * @param length
     * @return
     * @see RandomValidateCodeUtil#getRandom(String source, int length)
     */
    public static String getRandomLowerCaseLetters(int length) {
        return getRandom(LOWER_CASE_LETTERS, length);
    }

    /**
     * 随机得到一个固定长度的字符串(来源:自定义字符内容)
     *
     * @param source
     * @param length
     * @return <ul>
     *         <li>if source is null or empty, return null</li>
     *         <li>else see {@link RandomValidateCodeUtil#getRandom(char[] sourceChar, int length)}</li>
     *         </ul>
     */
    public static String getRandom(String source, int length) {
        return StringUtils.isEmpty(source) ? null : getRandom(source.toCharArray(), length);
    }

    /**
     * 随机得到一个固定长度的字符串(来源:自定义字符数组)
     *
     * @param sourceChar
     * @param length
     * @return <ul>
     *         <li>if sourceChar is null or empty, return null</li>
     *         <li>if length less than 0, return null</li>
     *         </ul>
     */
    public static String getRandom(char[] sourceChar, int length) {
        if (sourceChar == null || sourceChar.length == 0 || length < 0) {
            return null;
        }

        StringBuilder str = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            str.append(sourceChar[random.nextInt(sourceChar.length)]);
        }
        return str.toString();
    }

    /**
     * 得到一个0到指定数字之间的一位数
     *
     * @param max
     * @return <ul>
     *         <li>if max <= 0, return 0</li>
     *         <li>else return random int between 0 and max</li>
     *         </ul>
     */
    public static int getRandom(int max) {
        return getRandom(0, max);
    }

    /**
     * 得到指定上下限之间的一位数
     *
     * @param min
     * @param max
     * @return <ul>
     *         <li>if min > max, return 0</li>
     *         <li>if min == max, return min</li>
     *         <li>else return random int between min and max</li>
     *         </ul>
     */
    public static int getRandom(int min, int max) {
        if (min > max) {
            return 0;
        }
        if (min == max) {
            return min;
        }
        return min + new Random().nextInt(max - min);
    }

    /**
     * 洗牌算法,随机排列指定的数组使用默认的随机性
     *
     * @param objArray
     * @return
     */
    public static boolean shuffle(Object[] objArray) {
        if (objArray == null) {
            return false;
        }

        return shuffle(objArray, getRandom(objArray.length));
    }

    /**
     * 洗牌算法,随机排列指定的数组
     *
     * @param objArray
     * @param shuffleCount
     * @return
     */
    public static boolean shuffle(Object[] objArray, int shuffleCount) {
        int length;
        if (objArray == null || shuffleCount < 0 || (length = objArray.length) < shuffleCount) {
            return false;
        }

        for (int i = 1; i <= shuffleCount; i++) {
            int random = getRandom(length - i);
            Object temp = objArray[length - i];
            objArray[length - i] = objArray[random];
            objArray[random] = temp;
        }
        return true;
    }

    /**
     * 洗牌算法,随机排列指定int数组使用默认的随机性来源
     *
     * @param intArray
     * @return
     */
    public static int[] shuffle(int[] intArray) {
        if (intArray == null) {
            return null;
        }

        return shuffle(intArray, getRandom(intArray.length));
    }

    /**
     * 洗牌算法,随机排列指定int数组
     *
     * @param intArray
     * @param shuffleCount
     * @return
     */
    public static int[] shuffle(int[] intArray, int shuffleCount) {
        int length;
        if (intArray == null || shuffleCount < 0 || (length = intArray.length) < shuffleCount) {
            return null;
        }

        int[] out = new int[shuffleCount];
        for (int i = 1; i <= shuffleCount; i++) {
            int random = getRandom(length - i);
            out[i - 1] = intArray[random];
            int temp = intArray[length - i];
            intArray[length - i] = intArray[random];
            intArray[random] = temp;
        }
        return out;
    }

    /**
     * 获取验证码图片
     * @param width 验证码宽度
     * @param height 验证码高度
     * @return 验证码Bitmap对象
     */
    public synchronized static Bitmap makeValidateCode(int width, int height){
        return ValidateCodeGenerator.createBitmap(width, height);
    }

    /**
     * 获取验证码值
     * @return 验证码字符串
     */
    public synchronized static String gainValidateCodeValue(){
        return ValidateCodeGenerator.getCode();
    }


    /**
     * 取指定范围内的随机数
     * @param max
     * @param min
     * @return
     */
    public static int getRamdomNumber(int max, int min) {
        return new Random().nextInt(max) % (max - min + 1) + min;
    }
    /**
     * 随机生成验证码内部类
     *
     */
    final static class ValidateCodeGenerator{
        private static final char[] CHARS = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        };

        //default settings
        private static final int DEFAULT_CODE_LENGTH = 4;
        private static final int DEFAULT_FONT_SIZE = 20;
        private static final int DEFAULT_LINE_NUMBER = 3;
        private static final int BASE_PADDING_LEFT = 5, RANGE_PADDING_LEFT = 10, BASE_PADDING_TOP = 15, RANGE_PADDING_TOP = 10;
        private static final int DEFAULT_WIDTH = 60, DEFAULT_HEIGHT = 30;

        //variables
        private static String value;
        private static int padding_left, padding_top;
        private static Random random = new Random();

        private static Bitmap createBitmap(int width,int height) {
            padding_left = 0;
            //创建画布
            Bitmap bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bp);

            //随机生成验证码字符
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < DEFAULT_CODE_LENGTH; i++) {
                buffer.append(CHARS[random.nextInt(CHARS.length)]);
            }
            value = buffer.toString();

            //设置颜色
            c.drawColor(Color.WHITE);

            //设置画笔大小
            Paint paint = new Paint();
            paint.setTextSize(DEFAULT_FONT_SIZE);
            for (int i = 0; i < value.length(); i++) {
                //随机样式
                randomTextStyle(paint);
                padding_left += BASE_PADDING_LEFT + random.nextInt(RANGE_PADDING_LEFT);
                padding_top = BASE_PADDING_TOP + random.nextInt(RANGE_PADDING_TOP);
                c.drawText(value.charAt(i) + "", padding_left, padding_top, paint);
            }
            for (int i = 0; i < DEFAULT_LINE_NUMBER; i++) {
                drawLine(c, paint);
            }
            //保存
            c.save(Canvas.ALL_SAVE_FLAG);
            c.restore();

            return bp;
        }

        public static String getCode() {
            return value;
        }

        private static void randomTextStyle(Paint paint) {
            int color = randomColor(1);
            paint.setColor(color);
            paint.setFakeBoldText(random.nextBoolean());//true为粗体，false为非粗体
            float skewX = random.nextInt(11) / 10;
            skewX = random.nextBoolean() ? skewX : -skewX;
            paint.setTextSkewX(skewX); //float类型参数，负数表示右斜，整数左斜
            paint.setUnderlineText(true); //true为下划线，false为非下划线
            paint.setStrikeThruText(true); //true为删除线，false为非删除线
        }

        private static void drawLine(Canvas canvas, Paint paint) {
            int color = randomColor(1);
            int startX = random.nextInt(DEFAULT_WIDTH);
            int startY = random.nextInt(DEFAULT_HEIGHT);
            int stopX = random.nextInt(DEFAULT_WIDTH);
            int stopY = random.nextInt(DEFAULT_HEIGHT);
            paint.setStrokeWidth(1);
            paint.setColor(color);
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }

        private static int randomColor(int rate) {
            int red = random.nextInt(256) / rate;
            int green = random.nextInt(256) / rate;
            int blue = random.nextInt(256) / rate;
            return Color.rgb(red, green, blue);
        }
    }

}