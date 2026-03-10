import os

_models = {}
THRESH = "0.XX"
FLAG = os.getenv("FLAG", "PUCTF26{just_a_sample_flag_definitely_not_the_real_one_12345_abcde}")

def load_models():
	from ultralytics import YOLO
	if _models:
		return _models
	a_path = os.path.join(os.getcwd(), "models", "model_a.pt")
	b_path = os.path.join(os.getcwd(), "models", "model_b.pt")
	_models['a'] = YOLO(a_path)
	_models['b'] = YOLO(b_path)
	return _models

def check_image(path):
	models = load_models()
	results_a = models['a'](path)
	results_b = models['b'](path)

	# extract top1 from each
	class_name_a = class_name_b = None
	top1_conf_a = top1_conf_b = 0.0

	for r in results_a:
		top1_index = r.probs.top1
		top1_conf_a = float(r.probs.top1conf.item())
		class_name_a = r.names[top1_index]

	for r in results_b:
		top1_index = r.probs.top1
		top1_conf_b = float(r.probs.top1conf.item())
		class_name_b = r.names[top1_index]

	ok = False
	message = "Models disagree on the prediction."
	if class_name_a == class_name_b:
		message = f"Both models agree on: {class_name_a}"
		if class_name_a == "usagi":
			if top1_conf_a > THRESH and top1_conf_b > THRESH:
				ok = True
				message = "FLAG"
			else:
				message = "You are near the answer, try again."

	return {
		'class_a': class_name_a,
		'conf_a': top1_conf_a,
		'class_b': class_name_b,
		'conf_b': top1_conf_b,
		'ok': ok,
		'message': message,
		'flag': FLAG if ok else None,
	}