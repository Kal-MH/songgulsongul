LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

#opencv library
OPENCV_INSTALL_MODULES:=on
include ${OPENCVROOT}\native\jni\OpenCV.mk
