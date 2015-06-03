class Entity(object):
	"""docstring for Entity"""
	def __init__(self):
		super(Entity, self).__init__()
		self.uid = "0"
		self.pid = "0"
		self.score = 0.0
		self.review = ""
	
	def __str__(self):
		return self.uid+","+self.pid+","+str(self.score)

class User(object):
	"""docstring for user"""
	def __init__(self,name=""):
		super(User, self).__init__()
		self.uid = name
		self.movie = list()
		self.score = list()



		
	