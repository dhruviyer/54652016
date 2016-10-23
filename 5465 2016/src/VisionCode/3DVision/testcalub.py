import cv2
import numpy as np
import glob

# Load previously saved data
with np.load('calibmtx\webcam_calibration_ouput3.npz') as X:
    mtx, dist, _, _ = [X[i] for i in ('mtx','dist','rvecs','tvecs')]

def draw(img, corners, imgpts):
    corner = tuple(corners[0].ravel())
    img = cv2.line(img, corner, tuple(imgpts[0].ravel()), (255,0,0), 5)
    img = cv2.line(img, corner, tuple(imgpts[1].ravel()), (0,255,0), 5)
    img = cv2.line(img, corner, tuple(imgpts[2].ravel()), (0,0,255), 5)
    return img

criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 30, 0.001)

objp = np.zeros((7*9,3), np.float32)
objp[:,:2] = np.mgrid[0:9,0:7].T.reshape(-1,2)

axis = np.float32([[3,0,0], [0,3,0], [0,0,-3]]).reshape(-1,3)

#np.reshape(axis,(-1,3))

#print(objp)
images = glob.glob('*.jpg')

for fname in images:
    img = cv2.imread(fname)
    gray = cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)

    #cv2.imshow('Gray',gray)
    #cv2.waitKey(500)

    ret, corners = cv2.findChessboardCorners(gray, (9,7),None)

    if ret:
        corners2 = cv2.cornerSubPix(gray,corners,(11,11),(-1,-1),criteria)

        # Find the rotation and translation vectors.
        inliers, rvecs, tvecs = cv2.solvePnP(objectPoints=objp, imagePoints= corners2,cameraMatrix=mtx,distCoeffs=dist)

        #print('Row vector is: %s' % (rvecs))
        #print('T vector is: %s' % (tvecs))

        rodRotMat,jakobe = cv2.Rodrigues(rvecs)

        print(rodRotMat)
        imgpts, jac = cv2.projectPoints(axis, rvecs, tvecs, mtx, dist)


        img = draw(img,corners2,imgpts)
        cv2.imshow('img',img)

        cv2.waitKey(500)


cv2.destroyAllWindows()