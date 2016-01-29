/*
Name: Ahmed El Shantaly
ID: 0836885
Date: March 13, 2015
Assignment 3
*/
#include <jni.h>
#include <stdio.h>
#include "JNILIB.h"
#include "ParameterManager.h"
#include <string.h>

ParameterManager * p;

/*JNI C layer that connects the Java layer with the ParamerterManger Library*/
/*Basically calls the functions and gets back the same variables specified in the ParameterManager.c*/
JNIEXPORT jint JNICALL Java_JNILIB_createPM (JNIEnv * env, jobject this, jint size){

	if (!(p = PM_create(size)))
    {
        exit(0);
    }
    return 0;
}

JNIEXPORT jint JNICALL Java_JNILIB_destroy(JNIEnv *env, jobject this){
	int returnVal = PM_destroy(p);
	return returnVal;
}


JNIEXPORT jint JNICALL Java_JNILIB_parseFrom(JNIEnv *env, jobject this, jstring fp, jchar comment){
	FILE * input;

	const char * _nativeString = (*env)->GetStringUTFChars(env, fp, 0);

	input = fopen("temp", "w");
    fprintf(input, (char*)_nativeString);
    fclose(input);
    input = fopen("temp", "r");
	
	(*env)->ReleaseStringUTFChars(env, fp, _nativeString);

	int returnVal = PM_parseFrom(p,input,comment);
    fclose(input);

    return returnVal;
}



JNIEXPORT jint JNICALL Java_JNILIB_manage(JNIEnv *env, jobject this, jstring pname, jint ptype, jint required){

	const char * _nativeString = (*env)->GetStringUTFChars(env, pname, 0);

	int returnVal = PM_manage(p, (char*)_nativeString, ptype, required);
	
	(*env)->ReleaseStringUTFChars(env, pname, _nativeString);
	
	return returnVal;
}


JNIEXPORT jint JNICALL Java_JNILIB_hasValue(JNIEnv *env, jobject this, jstring pname){
	
	const char * _nativeString = (*env)->GetStringUTFChars(env, pname, 0);

	int returnVal = PM_hasValue(p, (char*)_nativeString);
	(*env)->ReleaseStringUTFChars(env, pname, _nativeString);

	return returnVal;

}


JNIEXPORT jint JNICALL Java_JNILIB_getIntValue(JNIEnv *env, jobject this, jstring pname){
	
	const char * _nativeString = (*env)->GetStringUTFChars(env, pname, 0);

	int value = PM_getValue(p, (char*)_nativeString).int_val;
	
	(*env)->ReleaseStringUTFChars(env, pname, _nativeString);

	return value;

}


JNIEXPORT jdouble JNICALL Java_JNILIB_getDoubleValue(JNIEnv *env, jobject this, jstring pname){	
	const char * _nativeString = (*env)->GetStringUTFChars(env, pname, 0);
	
	double value = PM_getValue(p, (char*)_nativeString).real_val;

	(*env)->ReleaseStringUTFChars(env, pname, _nativeString);

	return value;
}

JNIEXPORT jboolean JNICALL Java_JNILIB_getBoolValue(JNIEnv *env, jobject this, jstring pname){
	
	const char * _nativeString = (*env)->GetStringUTFChars(env, pname, 0);

	jboolean value = PM_getValue(p, (char*)_nativeString).bool_val;
	
	(*env)->ReleaseStringUTFChars(env, pname, _nativeString);

	return value;
}

JNIEXPORT jstring JNICALL Java_JNILIB_getStrValue(JNIEnv *env, jobject this, jstring pname){
	char * value = NULL;
	
	const char * _nativeString = (*env)->GetStringUTFChars(env, pname, 0);

	value = PM_getValue(p, (char*)_nativeString).str_val;

	jstring string = (*env)->NewStringUTF(env, value);

	(*env)->ReleaseStringUTFChars(env, pname, _nativeString);

	return string;
}


JNIEXPORT jstring JNICALL Java_JNILIB_getListValue (JNIEnv *env, jobject this, jstring pname){
	char * item = NULL;
	ParameterList* plist;
	
	const char * _nativeString = (*env)->GetStringUTFChars(env, pname, 0);

	plist = PM_getValue(p, (char*)_nativeString).list_val;
	item = PL_next(plist);

	(*env)->ReleaseStringUTFChars(env, pname, _nativeString);

	jstring string = (*env)->NewStringUTF(env, item);
 
    return string;

}

