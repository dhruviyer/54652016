import cv2
import numpy as np
import glob
import time
from threading import Thread

class WebcamVideoStream:
    def __init__(self, src=0):
        # initialize the video camera stream and read the first frame
        # from the stream
        self.stream = cv2.VideoCapture(src)
        #self.stream.set(3, 1920)
        #self.stream.set(4, 1080)
        self.stream.set(15,-5)
        (self.grabbed, self.frame) = self.stream.read()

        # initialize the variable used to indicate if the thread should
        # be stopped
        self.stopped = False

    def start(self):
        # start the thread to read frames from the video stream
        Thread(target=self.update, args=()).start()
        return self

    def update(self):
        # keep looping infinitely until the thread is stopped
        while True:
            # if the thread indicator variable is set, stop the thread
            if self.stopped:
                self.stream.release()
                return

            # otherwise, read the next frame from the stream
            (self.grabbed, self.frame) = self.stream.read()

    def read(self):
        # return the frame most recently read
        return self.frame

    def grabread(self):
        return self.grabbed

    def stop(self):
        # indicate that the thread should be stopped
        self.stopped = True


def draw(img, corners, imgpts):
    corner = tuple(corners[0].ravel())
    img = cv2.line(img, corner, tuple(imgpts[0].ravel()), (255, 0, 0), 5)
    img = cv2.line(img, corner, tuple(imgpts[1].ravel()), (0, 255, 0), 5)
    img = cv2.line(img, corner, tuple(imgpts[2].ravel()), (0, 0, 255), 5)
    return img

if __name__ == '__main__':

    with np.load('calibmtx\webcam_calibration_ouput3.npz') as X:
        mtx, dist, _, _ = [X[i] for i in ('mtx','dist','rvecs','tvecs')]

    criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 30, 0.001)

    objp = np.zeros((7*9,3), np.float32)
    objp[:,:2] = np.mgrid[0:9,0:7].T.reshape(-1,2)

    axis = np.float32([[3,0,0], [0,3,0], [0,0,-3]]).reshape(-1,3)

    #np.reshape(axis,(-1,3))

    print(objp)

    numframes = 0
    cap = WebcamVideoStream(src=0).start()
    start_time = time.time()

    while(True):
        img = cap.read()

        if(cap.grabread()):
            gray = cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)

            #cv2.imshow('Gray',gray)
            #cv2.waitKey(500)

            ret, corners = cv2.findChessboardCorners(gray, (9,7),None)

            if ret:
                corners2 = cv2.cornerSubPix(gray,corners,(11,11),(-1,-1),criteria)

                # Find the rotation and translation vectors.
                inliers, rvecs, tvecs = cv2.solvePnP(objectPoints=objp, imagePoints= corners2,cameraMatrix=mtx,distCoeffs=dist)

                imgpts, jac = cv2.projectPoints(axis, rvecs, tvecs, mtx, dist)

                #print(imgpts)
                img = draw(img,corners2,imgpts)

        cv2.imshow('img',img)

        numframes +=1
        if cv2.waitKey(1) & 0xFF == ord('x'):
            break

    cap.stop()
    totTime = time.time() - start_time
    print("--- %s seconds ---" % (totTime))
    print('----%s fps ----' % (numframes / totTime))

    cv2.destroyAllWindows()