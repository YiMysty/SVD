from Entity import Entity,User

def loadRawData():
	EntityGroup = []
	e = None
	for i,line in enumerate(open("movies.txt",'r')):
		if line.find("product/productId")>=0:
			EntityGroup.append(e)
			e = Entity()
			e.pid = line.strip().split(":")[1].replace(" ","")
		elif line.find("review/userId")>=0:
			e.uid = line.strip().split(":")[1].replace(" ","")
		elif line.find("review/score")>=0:
			e.score = float(line.strip().split(":")[1].replace(" ",""))
		elif line.find("review/summary")>=0:
			#e.review = line.strip().split(":")[1]
			continue
		else:
			continue
	return EntityGroup


def writeData(EntityGroup):
	writeStr = ""
	for e in EntityGroup:
		writeStr += str(e) + '\n'
	with open('_movies.txt','w') as f:
		f.write(writeStr)
	f.close()

#This is mainly map the user ID from ID to number start from 0..
#For convinient computation.
#Also map the movie ID to number.
count = 0
userMap = {}
movieMap = {}
formerPid = ""
movieID = 0
userId =  0
def secondProcess(filename):
	global count
	global movieID
	global userMap
	global movieMap
	localMovieMap = {}
	localUserMap  = {}
	global userId
	reWriteMessage = ""
	global formerPid
	for i,line in enumerate(open(filename,'r')):
		if line.find(",")>=0:
			count+=1
			if count%10000==0:
				print str(count)+'\n'
			info = line.split(",")
			uid = info[0]
			pid = info[1]
			score = info[2]
			if uid in userMap:
				uid = userMap[uid]
			else:
				userMap[uid] = userId
				localUserMap[uid] = userId
				uid = userId
				userId+=1
			if pid!=formerPid:
				formerPid = pid
				pid = movieID
				movieID+=1
				movieMap[movieID-1] = formerPid
				localMovieMap[movieID-1] = formerPid
			else:
				pid = movieID-1
			reWriteMessage = reWriteMessage + str(uid)+","+str(pid)+","+score+'\n' 
	
	with open(filename+'_data.txt','w') as f:
		f.write(reWriteMessage)
	f.close()
	reWriteMessage = ""
	for uid in localUserMap:
		reWriteMessage = reWriteMessage+str(uid)+","+str(localUserMap[uid])+'\n'
	with open(filename+'_usermap.txt','w') as f:
		f.write(reWriteMessage)
	f.close()
	reWriteMessage = ""
	for pid in localMovieMap:
		reWriteMessage = reWriteMessage+str(pid)+","+str(localMovieMap[pid])+'\n'
	with open(filename+'_moviemap.txt','w') as f:
		f.write(reWriteMessage)
	f.close()
	return


userInfoMap={}
def buildMap(filename):
	global userInfoMap
	for i,line in enumerate(open(filename,'r')):
		if i%10000==0:
			print i
		if len(line.split(","))<2:
			continue
		uid = line.split(",")[0]
		if int(uid)<1500 :
			if uid in userInfoMap:
				userInfoMap[uid].append([line.split(",")[1],line.split(",")[2]])
			else:
				userInfoMap[uid] = [[line.split(",")[1],line.split(",")[2]]]
	return

def main():
	#writeData(loadRawData())
	# for i in range(0,16):
	# 	secondProcess("xa"+chr(ord('a')+i))
	global userInfoMap
	for i in range(0,16):
		buildMap("xa"+chr(ord('a')+i)+"_data.txt")
		print i
	message = ""
	count = 0
	for uid in userInfoMap:
		count+=1
		print count
		for item in userInfoMap[uid]:
			message = message+uid+","+item[0]+","+item[1]+"\n"

	print "write to file"
	with open("smaller data with sort.txt",'w') as f:
		f.write(message)
	f.close()


if __name__ == '__main__':
	main()
	