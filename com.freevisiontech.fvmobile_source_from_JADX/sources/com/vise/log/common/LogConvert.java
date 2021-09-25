package com.vise.log.common;

import com.vise.log.parser.Parser;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogConvert {
    private static int getArrayDimension(Object object) {
        int dim = 0;
        int i = 0;
        while (i < object.toString().length() && object.toString().charAt(i) == '[') {
            dim++;
            i++;
        }
        return dim;
    }

    private static boolean isArray(Object object) {
        return object.getClass().isArray();
    }

    private static char getType(Object object) {
        if (!isArray(object)) {
            return 0;
        }
        String str = object.toString();
        return str.substring(str.lastIndexOf("[") + 1, str.lastIndexOf("[") + 2).charAt(0);
    }

    private static void traverseArray(StringBuilder result, Object array) {
        if (!isArray(array)) {
            result.append("not a array!!");
        } else if (getArrayDimension(array) == 1) {
            switch (getType(array)) {
                case 'B':
                    result.append(Arrays.toString((byte[]) array));
                    return;
                case 'D':
                    result.append(Arrays.toString((double[]) array));
                    return;
                case 'F':
                    result.append(Arrays.toString((float[]) array));
                    return;
                case 'I':
                    result.append(Arrays.toString((int[]) array));
                    return;
                case 'J':
                    result.append(Arrays.toString((long[]) array));
                    return;
                case 'L':
                    Object[] objects = (Object[]) array;
                    result.append("[");
                    for (int i = 0; i < objects.length; i++) {
                        result.append(objectToString(objects[i]));
                        if (i != objects.length - 1) {
                            result.append(",");
                        }
                    }
                    result.append("]");
                    return;
                case 'S':
                    result.append(Arrays.toString((short[]) array));
                    return;
                case 'Z':
                    result.append(Arrays.toString((boolean[]) array));
                    return;
                default:
                    result.append(Arrays.toString((Object[]) array));
                    return;
            }
        } else {
            result.append("[");
            for (int i2 = 0; i2 < ((Object[]) array).length; i2++) {
                traverseArray(result, ((Object[]) array)[i2]);
                if (i2 != ((Object[]) array).length - 1) {
                    result.append(",");
                }
            }
            result.append("]");
        }
    }

    private static String parseArray(Object array) {
        StringBuilder result = new StringBuilder();
        traverseArray(result, array);
        return result.toString();
    }

    public static String objectToString(Object object) {
        return objectToString(object, 0);
    }

    private static boolean isStaticInnerClass(Class cla) {
        if (cla == null || !cla.isMemberClass() || (cla.getModifiers() & 8) != 8) {
            return false;
        }
        return true;
    }

    private static String objectToString(Object object, int childLevel) {
        if (object == null) {
            return LogConstant.STRING_OBJECT_NULL;
        }
        if (childLevel > 2) {
            return object.toString();
        }
        if (LogConstant.getParsers() != null && LogConstant.getParsers().size() > 0) {
            for (Parser parser : LogConstant.getParsers()) {
                if (parser.parseClassType().isAssignableFrom(object.getClass())) {
                    return parser.parseString(object);
                }
            }
        }
        if (isArray(object)) {
            return parseArray(object);
        }
        if (!object.toString().startsWith(object.getClass().getName() + "@")) {
            return object.toString();
        }
        StringBuilder builder = new StringBuilder();
        getClassFields(object.getClass(), builder, object, false, childLevel);
        for (Class superClass = object.getClass().getSuperclass(); !superClass.equals(Object.class); superClass = superClass.getSuperclass()) {
            getClassFields(superClass, builder, object, true, childLevel);
        }
        return builder.toString();
    }

    private static void getClassFields(Class cla, StringBuilder builder, Object o, boolean isSubClass, int childOffset) {
        String obj;
        String obj2;
        String obj3;
        if (!cla.equals(Object.class)) {
            if (isSubClass) {
                builder.append(LogConstant.f1064BR).append(LogConstant.f1064BR).append("=> ");
            }
            builder.append(cla.getSimpleName()).append(" {");
            Field[] fields = cla.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                if (!cla.isMemberClass() || isStaticInnerClass(cla) || i != 0) {
                    Object subObject = null;
                    try {
                        Object subObject2 = field.get(o);
                        if (subObject2 != null) {
                            if (isStaticInnerClass(cla) || (!field.getName().equals("$change") && !field.getName().equalsIgnoreCase("this$0"))) {
                                if (subObject2 instanceof String) {
                                    subObject2 = "\"" + subObject2 + "\"";
                                } else if (subObject2 instanceof Character) {
                                    subObject2 = "'" + subObject2 + "'";
                                }
                                if (childOffset < 2) {
                                    subObject2 = objectToString(subObject2, childOffset + 1);
                                }
                            }
                        }
                        String formatString = "" + "%s = %s, ";
                        Object[] objArr = new Object[2];
                        objArr[0] = field.getName();
                        if (subObject2 == null) {
                            obj3 = "null";
                        } else {
                            obj3 = subObject2.toString();
                        }
                        objArr[1] = obj3;
                        builder.append(String.format(formatString, objArr));
                    } catch (IllegalAccessException e) {
                        Object subObject3 = e;
                        if (subObject3 != null) {
                            if (isStaticInnerClass(cla) || (!field.getName().equals("$change") && !field.getName().equalsIgnoreCase("this$0"))) {
                                if (subObject3 instanceof String) {
                                    subObject3 = "\"" + subObject3 + "\"";
                                } else if (subObject3 instanceof Character) {
                                    subObject3 = "'" + subObject3 + "'";
                                }
                                if (childOffset < 2) {
                                    subObject3 = objectToString(subObject3, childOffset + 1);
                                }
                            }
                        }
                        String formatString2 = "" + "%s = %s, ";
                        Object[] objArr2 = new Object[2];
                        objArr2[0] = field.getName();
                        if (subObject3 == null) {
                            obj2 = "null";
                        } else {
                            obj2 = subObject3.toString();
                        }
                        objArr2[1] = obj2;
                        builder.append(String.format(formatString2, objArr2));
                    } catch (Throwable th) {
                        if (0 != 0) {
                            if (isStaticInnerClass(cla) || (!field.getName().equals("$change") && !field.getName().equalsIgnoreCase("this$0"))) {
                                if (0 instanceof String) {
                                    subObject = "\"" + null + "\"";
                                } else if (0 instanceof Character) {
                                    subObject = "'" + null + "'";
                                }
                                if (childOffset < 2) {
                                    subObject = objectToString(subObject, childOffset + 1);
                                }
                            }
                        }
                        String formatString3 = "" + "%s = %s, ";
                        Object[] objArr3 = new Object[2];
                        objArr3[0] = field.getName();
                        if (subObject == null) {
                            obj = "null";
                        } else {
                            obj = subObject.toString();
                        }
                        objArr3[1] = obj;
                        builder.append(String.format(formatString3, objArr3));
                        throw th;
                    }
                }
            }
            if (builder.toString().endsWith("{")) {
                builder.append("}");
            } else {
                builder.replace(builder.length() - 2, builder.length() - 1, "" + "}");
            }
        }
    }

    public static String printDividingLine(int dir) {
        switch (dir) {
            case 1:
                return "╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════";
            case 2:
                return "╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════";
            case 3:
                return "║ ";
            case 4:
                return "╟───────────────────────────────────────────────────────────────────────────────────────────────────────────────────";
            default:
                return "";
        }
    }

    public static List<String> largeStringToList(String msg) {
        List<String> stringList = new ArrayList<>();
        int index = 0;
        int countOfSub = msg.length() / LogConstant.LINE_MAX;
        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                stringList.add(msg.substring(index, index + LogConstant.LINE_MAX));
                index += LogConstant.LINE_MAX;
            }
            stringList.add(msg.substring(index, msg.length()));
        } else {
            stringList.add(msg);
        }
        return stringList;
    }

    public static String shorten(String string, int count, int length) {
        if (string == null) {
            return null;
        }
        String resultString = string;
        if (Math.abs(length) < resultString.length()) {
            if (length > 0) {
                resultString = string.substring(0, length);
            }
            if (length < 0) {
                resultString = string.substring(string.length() + length, string.length());
            }
        }
        if (Math.abs(count) <= resultString.length()) {
            return resultString;
        }
        return String.format("%" + count + "s", new Object[]{resultString});
    }

    public static String shortenClassName(String className, int count, int maxLength) throws Exception {
        String className2 = shortenPackagesName(className, count);
        if (className2 == null) {
            return null;
        }
        if (maxLength == 0 || maxLength > className2.length()) {
            return className2;
        }
        if (maxLength < 0) {
            int maxLength2 = -maxLength;
            StringBuilder builder = new StringBuilder();
            int index = className2.length() - 1;
            while (true) {
                if (index <= 0) {
                    break;
                }
                int i = className2.lastIndexOf(46, index);
                if (i != -1) {
                    if (builder.length() > 0 && builder.length() + ((index + 1) - i) + 1 > maxLength2) {
                        builder.insert(0, '*');
                        break;
                    }
                    builder.insert(0, className2.substring(i, index + 1));
                } else if (builder.length() > 0 && builder.length() + index + 1 > maxLength2) {
                    builder.insert(0, '*');
                    break;
                } else {
                    builder.insert(0, className2.substring(0, index + 1));
                }
                index = i - 1;
            }
            return builder.toString();
        }
        StringBuilder builder2 = new StringBuilder();
        int index2 = 0;
        while (true) {
            if (index2 < className2.length()) {
                int i2 = className2.indexOf(46, index2);
                if (i2 != -1) {
                    if (builder2.length() > 0 && i2 + 1 > maxLength) {
                        builder2.insert(builder2.length(), '*');
                        break;
                    }
                    builder2.insert(builder2.length(), className2.substring(index2, i2 + 1));
                    index2 = i2 + 1;
                } else if (builder2.length() > 0) {
                    builder2.insert(builder2.length(), '*');
                } else {
                    builder2.insert(builder2.length(), className2.substring(index2, className2.length()));
                }
            } else {
                break;
            }
        }
        return builder2.toString();
    }

    private static String shortenPackagesName(String className, int count) {
        if (className == null) {
            return null;
        }
        if (count == 0) {
            return className;
        }
        StringBuilder builder = new StringBuilder();
        if (count > 0) {
            int points = 1;
            int index = 0;
            while (true) {
                if (index >= className.length()) {
                    break;
                }
                int i = className.indexOf(46, index);
                if (i == -1) {
                    builder.insert(builder.length(), className.substring(index, className.length()));
                    break;
                } else if (points == count) {
                    builder.insert(builder.length(), className.substring(index, i));
                    break;
                } else {
                    builder.insert(builder.length(), className.substring(index, i + 1));
                    index = i + 1;
                    points++;
                }
            }
        } else if (count < 0) {
            String exceptString = shortenPackagesName(className, -count);
            if (!className.equals(exceptString)) {
                return className.replaceFirst(exceptString + '.', "");
            }
            builder.insert(builder.length(), className.substring(className.lastIndexOf(46) + 1, className.length()));
        }
        return builder.toString();
    }
}
