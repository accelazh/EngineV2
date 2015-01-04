package com.accela.TestCases.objectStreams;

import com.accela.ObjectPool.ObjectPool;
import com.accela.ObjectStreams.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import junit.framework.TestCase;

public class TestingObjectStreams extends TestCase
{
	private HPObjectInputStream in;
	private HPObjectOutputStream out;

	private static final String FILE = "temp.txt";

	protected void setUp() throws Exception
	{
		super.setUp();
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
		if (in != null)
		{
			in.close();
		}
		if (out != null)
		{
			out.close();
		}

		in = null;
		out = null;
	}

	public void testObjectStreamsSimple()
	{
		try
		{
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

			out = new HPObjectOutputStream(new BufferedOutputStream(byteArray));

			char[] chars = new char[30];
			for (int i = 0; i < chars.length; i++)
			{
				chars[i] = (char) (i + 'A');
			}

			Character[] chars2 = new Character[30];
			for (int i = 0; i < chars2.length; i++)
			{
				chars2[i] = (char) (i + 'A');
			}

			out.writeObject(chars);

			out.writeObject(chars2);

			out.close();

			in = new HPObjectInputStream(new BufferedInputStream(
					new ByteArrayInputStream(byteArray.toByteArray())));

			char[] charsIn = (char[]) in.readObject();
			assert (charsIn.length == chars.length);
			for (int i = 0; i < chars.length; i++)
			{
				assert (charsIn[i] == chars[i]);
			}
			assert (charsIn != chars);

			Character[] chars2In = (Character[]) in.readObject();
			assert (chars2In.length == chars2.length);
			for (int i = 0; i < chars2.length; i++)
			{
				assert (chars2In[i].equals(chars2[i]));
			}
			assert (chars2In != chars2);

			in.close();

			// ====================
			ByteArrayOutputStream byteArray3 = new ByteArrayOutputStream();
			out = new HPObjectOutputStream(new BufferedOutputStream(byteArray3));

			char[][] charss = new char[30][40];
			Character[][] charss2 = new Character[30][40];

			for (int i = 0; i < charss.length; i++)
			{
				for (int j = 0; j < charss[i].length; j++)
				{
					charss[i][j] = (char) ('A' + j);
					charss2[i][j] = (char) ('A' + j);
				}
			}

			out.writeObject(charss);

			out.writeObject(charss2);

			out.close();

			in = new HPObjectInputStream(new BufferedInputStream(
					new ByteArrayInputStream(byteArray3.toByteArray())));

			char[][] charssIn = (char[][]) in.readObject();
			assert (charssIn.length == charss.length);
			for (int i = 0; i < charss.length; i++)
			{
				assert (charss[i].length == charssIn[i].length);
				for (int j = 0; j < charss[i].length; j++)
				{
					assert (charssIn[i][j] == charss[i][j]);
				}

			}
			assert (charssIn != charss);

			Character[][] charss2In = (Character[][]) in.readObject();
			assert (charss2In.length == charss2.length);
			for (int i = 0; i < charss2.length; i++)
			{
				assert (charss2[i].length == charss2In[i].length);
				for (int j = 0; j < charss2[i].length; j++)
				{
					assert (charss2In[i][j].equals(charss2[i][j]));
				}
			}
			assert (charss2In != charss2);

			in.close();

			// ====================

			ByteArrayOutputStream byteArray2 = new ByteArrayOutputStream();

			out = new HPObjectOutputStream(new BufferedOutputStream(byteArray2));

			String str = "today is not so good!";

			out.writeObject(str);

			out.close();

			in = new HPObjectInputStream(new BufferedInputStream(
					new ByteArrayInputStream(byteArray2.toByteArray())));
			Object obj = in.readObject();

			in.close();

			assert (str.equals(obj));
			assert (str != obj);

			// =============

			out = new HPObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(FILE)));

			out.writeObject(null);

			out.close();

			in = new HPObjectInputStream(new BufferedInputStream(
					new FileInputStream(FILE)));

			assert (in.readObject() == null);

			in.close();
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}

	}

	public void testObjectStream()
	{
		try
		{
			out = new HPObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(FILE)));

			Object[] objects = new Object[] { new ClassA(1), new ClassAA(2),
					new ClassAB(), new ClassABA(), new ClassB(3), new ClassC(),
					new ClassD("b"), };

			for (int i = 0; i < objects.length; i++)
			{
				out.writeObject(objects[i]);
			}

			out.close();

			in = new HPObjectInputStream(new BufferedInputStream(
					new FileInputStream(FILE)));

			Object[] objectsIn = new Object[objects.length];
			for (int i = 0; i < objectsIn.length; i++)
			{
				objectsIn[i] = in.readObject();
			}

			in.close();

			for (int i = 0; i < objects.length; i++)
			{
				assert (objects[i] != null);
				assert (objectsIn[i] != null);

				assert (objects[i].equals(objectsIn[i]));
				assert (objectsIn[i].equals(objects[i]));
			}

		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}
	}

	public void testObjectStreamRandom()
	{
		try
		{
			for (int i = 0; i < 1000; i++)
			{
				checkObjectStreamRandom();
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}
	}

	private void checkObjectStreamRandom() throws IOException,
			ClassNotFoundException, InvocationTargetException,
			InstantiationException
	{
		Object[] objs = generateRandomObjects();
		Object[] readObjs = new Object[objs.length];

		out = new HPObjectOutputStream(new BufferedOutputStream(
				new FileOutputStream(FILE)));
		writeObjects(objs);
		out.close();

		in = new HPObjectInputStream(new BufferedInputStream(
				new FileInputStream(FILE)));
		readObjects(readObjs);
		in.close();

		for (int i = 0; i < objs.length; i++)
		{
			assert ((objs[i] == null && readObjs[i] == null) || (objs[i] != null && readObjs[i] != null));

			if (objs[i] != null)
			{
				assert (objs[i].equals(readObjs[i]));
				assert (readObjs[i].equals(objs[i]));
				assert (objs[i] != readObjs[i]);
			}
		}

	}

	private void readObjects(Object[] objs) throws IOException,
			ClassNotFoundException, InvocationTargetException,
			InstantiationException
	{
		for (int i = 0; i < objs.length; i++)
		{
			objs[i] = in.readObject();
		}
	}

	private void writeObjects(Object[] objs) throws IOException
	{
		for (Object obj : objs)
		{
			out.writeObject(obj);
		}
	}

	private Object[] generateRandomObjects()
	{
		Random rand = new Random();
		Object[] objs = new Object[rand.nextInt(16)];

		for (int i = 0; i < objs.length; i++)
		{
			switch (rand.nextInt(8))
			{
			case 0:
				objs[i] = new ClassA(10);
				break;
			case 1:
				objs[i] = new ClassAA(10);
				break;
			case 2:
				objs[i] = new ClassAB();
				break;
			case 3:
				objs[i] = new ClassABA();
				break;
			case 4:
				objs[i] = new ClassB(17.0);
				break;
			case 5:
				objs[i] = new ClassC();
				break;
			case 6:
				objs[i] = new ClassD("go to hell!!");
				break;
			case 7:
				objs[i] = null;
				break;
			default:
				assert (false);
				break;
			}
		}

		return objs;
	}

	public void testArrayRandom() throws IOException, ClassNotFoundException,
			InvocationTargetException, InstantiationException
	{
		try
		{
			for (int i = 0; i < 1000; i++)
			{
				checkArrayRandom();
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}
	}

	private void checkArrayRandom() throws IOException, ClassNotFoundException,
			InvocationTargetException, InstantiationException
	{
		Object[] objs = generateRandomObjects();
		Object[] readObjs = new Object[objs.length];

		out = new HPObjectOutputStream(new BufferedOutputStream(
				new FileOutputStream(FILE)));
		out.writeObject(objs);
		out.close();

		in = new HPObjectInputStream(new BufferedInputStream(
				new FileInputStream(FILE)));
		readObjs = (Object[]) in.readObject();
		in.close();

		assert (readObjs != null);
		for (int i = 0; i < objs.length; i++)
		{
			assert ((objs[i] == null && readObjs[i] == null) || (objs[i] != null && readObjs[i] != null));

			if (objs[i] != null)
			{
				assert (objs[i].equals(readObjs[i]));
				assert (readObjs[i].equals(objs[i]));
				assert (objs[i] != readObjs[i]);
			}
		}
		assert (readObjs != objs);

	}

	@SuppressWarnings("unchecked")
	public void testCircularReference()
	{
		try
		{
			ClassE classE = new ClassE();
			ClassF classF = new ClassF();
			classE.setClassF(classF);
			classF.setClassE(classE);

			out = new HPObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(FILE)));

			out.writeObject(classE);
			out.writeObject(classF);

			out.close();

			in = new HPObjectInputStream(new BufferedInputStream(
					new FileInputStream(FILE)));

			ClassE classEIn = (ClassE) in.readObject();
			ClassF classFIn = (ClassF) in.readObject();

			in.close();

			assert (classEIn.equals(classE));
			assert (classE.equals(classEIn));
			assert (classEIn != classE);
			assert (classEIn.getClassF().getClassE() == classEIn);
			assert (classEIn.getClassF() != classE.getClassF());

			assert (classFIn.getClassE().equals(classF.getClassE()));
			assert (classF.getClassE().equals(classFIn.getClassE()));
			assert (classFIn != classF);
			assert (classFIn.getClassE().getClassF() == classFIn);
			assert (classFIn.getClassE() != classF.getClassE());

			// ////////////////////////////////////////////////////

			ClassG classG = new ClassG();
			ClassH classH = new ClassH();
			ClassI classI = new ClassI();

			classG.setClassH(classH);
			classH.setClassI(classI);
			classI.setClassG(classG);

			out = new HPObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(FILE)));

			out.writeObject(classG);

			out.close();

			in = new HPObjectInputStream(new BufferedInputStream(
					new FileInputStream(FILE)));

			ClassG classGIn = (ClassG) in.readObject();

			in.close();

			assert (classG.equals(classGIn));
			assert (classGIn.equals(classG));

			// ///////////////////////////////////////////////

			ClassCenter c = new ClassCenter();
			ClassLeftUp lu = new ClassLeftUp();
			ClassRightUp ru = new ClassRightUp();
			ClassLeftDown ld = new ClassLeftDown();
			ClassRightDown rd = new ClassRightDown();

			c.setClassLeftDown(ld);
			c.setClassLeftUp(lu);
			c.setClassRightDown(rd);
			c.setClassRightUp(ru);

			lu.setClassCenter(c);
			lu.setClassLeftDown(ld);
			lu.setClassRightUp(ru);

			ru.setClassCenter(c);
			ru.setClassLeftUp(lu);
			ru.setClassRightDown(rd);

			ld.setClassCenter(c);
			ld.setClassLeftUp(lu);
			ld.setClassRightDown(rd);

			rd.setClassCenter(c);
			rd.setClassLeftDown(ld);
			rd.setClassRightUp(ru);

			out = new HPObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(FILE)));

			out.writeObject(c);

			out.close();

			in = new HPObjectInputStream(new BufferedInputStream(
					new FileInputStream(FILE)));

			ClassCenter cIn = (ClassCenter) in.readObject();

			in.close();

			ClassLeftUp luIn = cIn.getClassLeftUp();
			ClassLeftDown ldIn = cIn.getClassLeftDown();
			ClassRightUp ruIn = cIn.getClassRightUp();
			ClassRightDown rdIn = cIn.getClassRightDown();

			assert (cIn != null);
			assert (cIn.equals(c));
			assert (cIn != c);

			assert (luIn != null);
			assert (luIn.equals(lu));
			assert (luIn != lu);

			assert (ruIn != null);
			assert (ruIn.equals(ru));
			assert (ruIn != ru);

			assert (rdIn != null);
			assert (rdIn.equals(rd));
			assert (rdIn != rd);

			assert (ldIn != null);
			assert (ldIn.equals(ld));
			assert (ldIn != ld);

			assert (luIn.getClassCenter() == cIn);
			assert (ruIn.getClassCenter() == cIn);
			assert (rdIn.getClassCenter() == cIn);
			assert (ldIn.getClassCenter() == cIn);

			assert (luIn.getClassLeftDown() == ldIn);
			assert (luIn.getClassRightUp() == ruIn);

			assert (ruIn.getClassLeftUp() == luIn);
			assert (ruIn.getClassRightDown() == rdIn);

			assert (rdIn.getClassLeftDown() == ldIn);
			assert (rdIn.getClassRightUp() == ruIn);

			assert (ldIn.getClassLeftUp() == luIn);
			assert (ldIn.getClassRightDown() == rdIn);

			// //////////////////////////////////////////////////

			ClassSelf cs1 = new ClassSelf();
			cs1.setId(1);
			ClassSelf cs2 = new ClassSelf();
			cs2.setId(2);

			cs1.setClassSelf(cs2);
			cs2.setClassSelf(cs1);

			out = new HPObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(FILE)));

			out.writeObject(cs1);

			out.close();

			in = new HPObjectInputStream(new BufferedInputStream(
					new FileInputStream(FILE)));

			ClassSelf cs1In = (ClassSelf) in.readObject();

			in.close();

			assert (cs1In != cs1);
			assert (cs1In.equals(cs1));
			assert (cs1In.getClassSelf().equals(cs1.getClassSelf()));
			assert (cs1In.getClassSelf().getClassSelf() == cs1In);
			assert (cs1In.getClassSelf() != cs1.getClassSelf());

			// //////////////////////////////////////////////////

			ClassSelf cs3 = new ClassSelf();
			cs3.setId(3);

			cs1.setClassSelf(cs2);
			cs2.setClassSelf(cs3);
			cs3.setClassSelf(cs1);

			out = new HPObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(FILE)));

			out.writeObject(cs1);

			out.close();

			in = new HPObjectInputStream(new BufferedInputStream(
					new FileInputStream(FILE)));

			cs1In = (ClassSelf) in.readObject();

			in.close();

			assert (cs1In.equals(cs1));
			assert (cs1In.getClassSelf().equals(cs1.getClassSelf()));
			assert (cs1In.getClassSelf().getClassSelf().equals(cs1
					.getClassSelf().getClassSelf()));

			assert (cs1In.getClassSelf().getClassSelf().getClassSelf() == cs1In);

			assert (cs1In != cs1);
			assert (cs1In.getClassSelf() != cs1.getClassSelf());
			assert (cs1In.getClassSelf().getClassSelf() != cs1.getClassSelf()
					.getClassSelf());

			// //////////////////////////////////////////////////

			ClassSelf cs0 = new ClassSelf();
			cs3.setId(0);

			cs0.setClassSelf(cs1);
			cs1.setClassSelf(cs2);
			cs2.setClassSelf(cs3);
			cs3.setClassSelf(cs1);

			out = new HPObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(FILE)));

			out.writeObject(cs0);

			out.close();

			in = new HPObjectInputStream(new BufferedInputStream(
					new FileInputStream(FILE)));

			ClassSelf cs0In = (ClassSelf) in.readObject();

			in.close();

			assert (cs0In.equals(cs0));
			assert (cs0In != cs0);

			cs1In = cs0In.getClassSelf();
			assert (cs1In.equals(cs1));
			assert (cs1In.getClassSelf().equals(cs1.getClassSelf()));
			assert (cs1In.getClassSelf().getClassSelf().equals(cs1
					.getClassSelf().getClassSelf()));

			assert (cs1In.getClassSelf().getClassSelf().getClassSelf() == cs1In);

			assert (cs1In != cs1);
			assert (cs1In.getClassSelf() != cs1.getClassSelf());
			assert (cs1In.getClassSelf().getClassSelf() != cs1.getClassSelf()
					.getClassSelf());

			// ////////////////////////////////

			List<String> list = new LinkedList<String>();
			for (int i = 0; i < 100; i++)
			{
				list.add("" + Math.random());
			}

			out = new HPObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(FILE)));

			out.writeObject(list);

			out.close();

			in = new HPObjectInputStream(new BufferedInputStream(
					new FileInputStream(FILE)));

			List<String> listIn = (List<String>) in.readObject();

			in.close();

			assert (listIn != list);
			assert (listIn.equals(list));

			assert (listIn.size() == list.size());
			for (int i = 0; i < list.size(); i++)
			{
				assert (list.get(i) != listIn.get(i));
				assert (list.get(i).equals(listIn.get(i)));
			}

			// //////////////////////////////////////////////////////

			ClassJ classJ = new ClassJ();
			for (int i = 0; i < 100; i++)
			{
				classJ.addClassK(new ClassK());
			}

			out = new HPObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(FILE)));

			out.writeObject(classJ);

			for (int i = 0; i < classJ.getClassKs().size(); i++)
			{
				out.writeObject(classJ.getClassKs().get(i));
			}

			out.close();

			in = new HPObjectInputStream(new BufferedInputStream(
					new FileInputStream(FILE)));

			ClassJ classJIn = (ClassJ) in.readObject();
			assert (classJIn.equals(classJ));
			assert (classJ.equals(classJIn));
			assert (classJIn != classJ);

			for (int i = 0; i < classJ.getClassKs().size(); i++)
			{
				ClassK classKIn = (ClassK) in.readObject();
				assert (classKIn != null);
				assert (classKIn.getClassJ() != null);
				assert (classKIn.getClassJ().equals(classJ));
				assert (classJ.equals(classKIn.getClassJ()));
				assert (classKIn.getClassJ() != classJ);
			}

			in.close();

			
			//////////////////////////////////////////////////////////////
			
			ClassL classL = new ClassL();
			for (int i = 0; i < 100; i++)
			{
				classL.addClassM(new ClassM());
			}
			for(int i=0;i<100;i++)
			{
				classL.addClassM(null);
			}

			out = new HPObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(FILE)));

			out.writeObject(classL);

			for (int i = 0; i < classL.getClassMs().size(); i++)
			{
				out.writeObject(classL.getClassMs().get(i));
			}

			out.close();

			in = new HPObjectInputStream(new BufferedInputStream(
					new FileInputStream(FILE)));

			ClassL classLIn = (ClassL) in.readObject();
			assert (classLIn.equals(classL));
			assert (classL.equals(classLIn));
			assert (classLIn != classL);

			for (int i = 0; i < classL.getClassMs().size(); i++)
			{
				ClassM classMIn = (ClassM) in.readObject();
				if(i>=100)
				{
					assert(null==classMIn);
					continue;
				}

				
				assert (classMIn.getClassL() != null);
				assert (classMIn.getClassL().equals(classL));
				assert (classL.equals(classMIn.getClassL()));
				assert (classMIn.getClassL() != classL);
			}

			in.close();

		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}

	}

	@SuppressWarnings("unchecked")
	public void testCollections()
	{
		try
		{
			List<String> strList = new LinkedList<String>();

			Set<Integer> intSet = new HashSet<Integer>();

			Map<Long, String> lsMap = new ConcurrentHashMap<Long, String>();

			List<List<String>> strListList = new LinkedList<List<String>>();

			for (int i = 0; i < 100; i++)
			{
				strList.add("" + i);
				intSet.add(2 * i);
				lsMap.put(3L * i, "" + 3 * i);

				List<String> list = new LinkedList<String>();
				for (int j = 0; j < 100; j++)
				{
					list.add("" + Math.random());
				}

				strListList.add(list);
			}

			out = new HPObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(FILE)));

			out.writeObject(strList);
			out.writeObject(intSet);
			out.writeObject(lsMap);

			out.writeObject(strListList);

			out.close();

			in = new HPObjectInputStream(new BufferedInputStream(
					new FileInputStream(FILE)));

			List<String> strListIn = (List<String>) in.readObject();
			Set<Integer> intSetIn = (Set<Integer>) in.readObject();
			Map<Long, String> lsMapIn = (Map<Long, String>) in.readObject();
			List<List<String>> strListListIn = (List<List<String>>) in
					.readObject();

			in.close();

			assert (strListIn.equals(strList));
			assert (intSetIn.equals(intSet));
			assert (lsMapIn.equals(lsMap));
			assert (strListListIn.equals(strListList));

			assert (strListIn.get(0) != strList.get(0));
			assert (strListListIn.get(0) != strListList.get(0));
			assert (strListListIn.get(0).get(0) != strListList.get(0).get(0));

			// //////////////////////////////////////////////////////

			ClassCollections cc = new ClassCollections();

			out = new HPObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(FILE)));

			out.writeObject(cc);

			out.close();

			in = new HPObjectInputStream(new BufferedInputStream(
					new FileInputStream(FILE)));

			ClassCollections ccIn = (ClassCollections) in.readObject();

			in.close();

			assert (ccIn != null);
			assert (ccIn != cc);
			assert (cc.equals(ccIn));
			assert (ccIn.equals(cc));
			assert (ccIn.isObjTransiented());

		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}
	}

	public void testObjectPool()
	{
		try
		{
			ClassPoolViewer cpv = new ClassPoolViewer();
			int count = ClassPoolViewer.getCreateCount();

			out = new HPObjectOutputStream(new BufferedOutputStream(
					new FileOutputStream(FILE)));

			out.writeObject(cpv);

			out.close();

			ObjectPool.put(cpv);

			in = new HPObjectInputStream(new BufferedInputStream(
					new FileInputStream(FILE)));

			ClassPoolViewer cpvIn = (ClassPoolViewer) in.readObject();

			in.close();

			assert (cpvIn.equals(cpv));
			assert (count == ClassPoolViewer.getCreateCount());

		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}
	}

	public static void main(String[] args) throws Exception
	{
		/*HPObjectOutputStream out=null;
		HPObjectInputStream in=null;
		
		ClassJ classJ = new ClassJ();
		for (int i = 0; i < 100; i++)
		{
			classJ.addClassK(new ClassK());
		}

		out = new HPObjectOutputStream(new BufferedOutputStream(
				new FileOutputStream(FILE)));

		out.writeObject(classJ);

		for (int i = 0; i < classJ.getClassKs().size(); i++)
		{
			out.writeObject(classJ.getClassKs().get(i));
		}

		out.close();

		in = new HPObjectInputStream(new BufferedInputStream(
				new FileInputStream(FILE)));

		ClassJ classJIn = (ClassJ) in.readObject();
		assert (classJIn.equals(classJ));
		assert (classJ.equals(classJIn));
		assert (classJIn != classJ);

		for (int i = 0; i < classJ.getClassKs().size(); i++)
		{
			ClassK classKIn = (ClassK) in.readObject();
			assert (classKIn != null);
			assert (classKIn.getClassJ() != null);
			assert (classKIn.getClassJ().equals(classJ));
			assert (classJ.equals(classKIn.getClassJ()));
			assert (classKIn.getClassJ() != classJ);
		}

		in.close();*/
		
		/*TestingObjectStreams t=new TestingObjectStreams();
		t.setUp();
		t.testCircularReference();
		t.tearDown();*/
		
	}

}
