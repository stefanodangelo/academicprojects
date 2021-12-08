import cv2, time, sys

# webcam
PC_WEBCAM = 0
EXT_WEBCAM = 1
USB_WEBCAM = 2
IP_WEBCAM = "rtsp://camera:jeestunautre@192.168.1.142:554/stream1"

# parametri
SECONDS = 20 # durata della registrazione del video
TIME_FACTOR = 20.0  # regola la velocità di riproduzione (valori più alti -> video più lento)

# costanti
SPEED_FACTOR = 2.0  # fattore di rallentamento
SCREEN_SIZE = (1920, 1080)
SCREENS = {"main": 0,
           "first": 1}
CLOSE = 'x'  # tasto per chiudere


def read_key(time):
    return cv2.waitKey(time) & 0xFF


def acquire_video(camera, screen):
    start = time.perf_counter()

    winname = 'Capture Video'
    cv2.namedWindow(winname, cv2.WND_PROP_FULLSCREEN)
    cv2.moveWindow(winname, SCREEN_SIZE[0] * screen, 0)
    cv2.setWindowProperty(winname, cv2.WND_PROP_FULLSCREEN, cv2.WINDOW_FULLSCREEN)

    frames = []
    while camera.isOpened():
        # cattura ogni frame del video
        _, frame = camera.read()

        elapsed_time = time.perf_counter() - start
        if elapsed_time <= SECONDS:
            frames.append(frame)
        else:
            break

        cv2.moveWindow(winname, SCREEN_SIZE[0] * screen, 0)
        cv2.imshow(winname, frame)

        if read_key(1) == ord(CLOSE):
            sys.exit()

    # chiude la finestra e de-alloca la memoria usata
    cv2.destroyWindow(winname)

    return frames

def play_video(frames, screen):
    winname = 'Je Est Un Autre'

    cv2.namedWindow(winname, cv2.WND_PROP_FULLSCREEN)
    cv2.moveWindow(winname, SCREEN_SIZE[0] * screen, 0)
    cv2.setWindowProperty(winname, cv2.WND_PROP_FULLSCREEN, cv2.WINDOW_FULLSCREEN)

    for frame in frames:
        cv2.moveWindow(winname, SCREEN_SIZE[0] * screen, 0)
        cv2.imshow(winname, frame)

        time_to_wait = int(TIME_FACTOR * SPEED_FACTOR**2)

        if read_key(time_to_wait) == ord(CLOSE):
            sys.exit()

    # chiude la finestra e de-alloca la memoria usata
    cv2.destroyWindow(winname)


if __name__ == '__main__':
    camera = cv2.VideoCapture(PC_WEBCAM)
    screen = SCREENS['first']     # <- usare 'first' per vedere il tutto tramite proiettore

    while (True):
        frames = acquire_video(camera, screen)
        play_video(frames, screen)

        del frames

"""
# prendi massima risoluzione video
camera.set(cv2.CAP_PROP_FRAME_WIDTH, 100000000)
camera.set(cv2.CAP_PROP_FRAME_HEIGHT, 100000000)
width = int(camera.get(cv2.CAP_PROP_FRAME_WIDTH))
height = int(camera.get(cv2.CAP_PROP_FRAME_HEIGHT))

# setting necessari per aumentare la risoluzione del video
camera.set(cv2.CAP_PROP_FRAME_WIDTH, width * 2)
camera.set(cv2.CAP_PROP_FRAME_HEIGHT, height * 2)
"""