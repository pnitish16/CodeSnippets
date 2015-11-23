# CodeSnippetspackage com.vigiguard.adapters;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.utils.Helper;
import com.vigiguard.R;
import com.vigiguard.interfaces.selectImgListener;
import com.vigiguard.models.ImageObject;

public class PicturesAdapter extends BaseAdapter
{
	private LayoutInflater inflater = null;
	private Activity activity;
	ArrayList<ImageObject> piclist;
	Uri selectedImageUri;
	PicturesAdapter mAdapter;
	DisplayImageOptions defaultOptions;
	ImageLoader imageLoader = ImageLoader.getInstance();
	selectImgListener listener;
	private static int MAX_IMAGE_DIMENSION = 1024;
	private Context context;

	public PicturesAdapter(Activity a, ArrayList<ImageObject> arraList)
	{

		activity = a;
		context = a;
		piclist = arraList;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		defaultOptions = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().showImageForEmptyUri(R.drawable.ic_launcher).showImageOnFail(R.drawable.ic_launcher)
				.showStubImage(R.drawable.ic_launcher).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).considerExifParams(true).build();
	}

	@Override
	public int getCount()
	{
		return piclist.size();
	}

	@Override
	public Object getItem(int position)
	{
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	public static class ViewHolder
	{
		ImageView ib_picture;
		ImageView imagebuttonRemove;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		final ViewHolder holder;
		

		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.list_item_image, parent, false);
			holder = new ViewHolder();
			holder.ib_picture = (ImageView) convertView.findViewById(R.id.ivImage);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		// holder.ib_picture.setOnClickListener(new OnClickListener()
		// {
		// @Override
		// public void onClick(View v)
		// {
		// // TODO Auto-generated method stub
		// }
		// });

		// if (piclist.get(position).isSelected())
		// holder.imagebuttonRemove.setVisibility(View.VISIBLE);
		// else
		// holder.imagebuttonRemove.setVisibility(View.GONE);
		//
		// holder.imagebuttonRemove.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View arg0)
		// {
		// if (piclist.size() > position)
		// {
		// piclist.remove(position);
		// }
		// notifyDataSetChanged();
		// }
		// });

		try
		{
			 ImageLoader.getInstance().displayImage("file://" +
			 piclist.get(position).getPath(), holder.ib_picture,
			 defaultOptions);
//			Uri imgUri = Uri.fromFile(new File(piclist.get(position).getPath()));
//			// Bitmap bitmap = scaleImage(context, imgUri);
//
//			Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imgUri);
//			bitmap = Bitmap.createScaledBitmap(bitmap, 266 * 3, 163 * 3, false);
//
//			// Bitmap bitmap = scaleImage(context, imgUri);
//			// Bitmap bitmap = ((BitmapDrawable)
//			// holder.ib_picture.getDrawable()).getBitmap();
//			// int rotation = getCameraPhotoOrientation(context, imgUri,
//			// piclist.get(position).getPath());
//
//			// Helper.Log("image" + position, ""+ rotation);
//			// bitmap = rotateImage(bitmap,rotation);
//
//			ExifInterface exif = null;
//			try
//			{
//				exif = new ExifInterface(piclist.get(position).getPath());
//			} catch (IOException e)
//			{
//				e.printStackTrace();
//			}
//			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
//
//			// if(orientation > 0 )
//			// orientation = orientation - 90;
//
//			bitmap = rotateBitmap(bitmap, orientation);
//			// getting bitmap from imageview
//			//
//			// ExifInterface ei = new
//			// ExifInterface(piclist.get(position).getPath());
//			// int orientation =
//			// ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//			// ExifInterface.ORIENTATION_NORMAL);
//			//
//			// switch (orientation)
//			// {
//			// case ExifInterface.ORIENTATION_ROTATE_90:
//			// bitmap = rotateImage(bitmap, 90);
//			// break;
//			// case ExifInterface.ORIENTATION_ROTATE_180:
//			// bitmap = rotateImage(bitmap, 180);
//			// break;
//			// }
//			//
//			holder.ib_picture.setImageBitmap(bitmap);
			// etc.
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return convertView;
	}

	public void setListener(selectImgListener listener)
	{
		this.listener = listener;
	}

	public static Bitmap rotateImage(Bitmap source, float angle)
	{
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, int orientation)
	{

		Matrix matrix = new Matrix();
		switch (orientation)
		{
			case ExifInterface.ORIENTATION_NORMAL:
				return bitmap;
			case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
				matrix.setScale(-1, 1);
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				matrix.setRotate(180);
				break;
			case ExifInterface.ORIENTATION_FLIP_VERTICAL:
				matrix.setRotate(180);
				matrix.postScale(-1, 1);
				break;
			case ExifInterface.ORIENTATION_TRANSPOSE:
				matrix.setRotate(90);
				matrix.postScale(-1, 1);
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				matrix.setRotate(90);
				break;
			case ExifInterface.ORIENTATION_TRANSVERSE:
				matrix.setRotate(-90);
				matrix.postScale(-1, 1);
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				matrix.setRotate(-90);
				break;
			default:
				return bitmap;
		}
		try
		{
			Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			bitmap.recycle();
			return bmRotated;
		} catch (OutOfMemoryError e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap scaleImage(Context context, Uri photoUri) throws IOException
	{
		InputStream is = context.getContentResolver().openInputStream(photoUri);
		BitmapFactory.Options dbo = new BitmapFactory.Options();
		dbo.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, dbo);
		is.close();

		int rotatedWidth, rotatedHeight;
		int orientation = getOrientation(context, photoUri);

		if (orientation == 90 || orientation == 270)
		{
			rotatedWidth = dbo.outHeight;
			rotatedHeight = dbo.outWidth;
		}
		else
		{
			rotatedWidth = dbo.outWidth;
			rotatedHeight = dbo.outHeight;
		}

		Bitmap srcBitmap;
		is = context.getContentResolver().openInputStream(photoUri);
		if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION)
		{
			float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
			float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
			float maxRatio = Math.max(widthRatio, heightRatio);

			// Create the bitmap from file
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = (int) maxRatio;
			srcBitmap = BitmapFactory.decodeStream(is, null, options);
		}
		else
		{
			srcBitmap = BitmapFactory.decodeStream(is);
		}
		is.close();

		/*
		 * if the orientation is not 0 (or -1, which means we don't know), we
		 * have to do a rotation.
		 */
		if (orientation > 0)
		{
			Matrix matrix = new Matrix();
			matrix.postRotate(orientation);

			srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
		}

		String type = context.getContentResolver().getType(photoUri);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (type.equals("image/png"))
		{
			srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		}
		else if (type.equals("image/jpg") || type.equals("image/jpeg"))
		{
			srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		}
		byte[] bMapArray = baos.toByteArray();
		baos.close();
		return BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
	}

	public static int getOrientation(Context context, Uri photoUri)
	{
		/* it's on the external media. */
		Cursor cursor = context.getContentResolver().query(photoUri, new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

		if (cursor.getCount() != 1)
		{
			return -1;
		}

		cursor.moveToFirst();
		return cursor.getInt(0);
	}

	// get camera orientation
	public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath)
	{
		int rotate = 0;
		try
		{
			context.getContentResolver().notifyChange(imageUri, null);
			File imageFile = new File(imagePath);

			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

			switch (orientation)
			{
				case ExifInterface.ORIENTATION_ROTATE_270:
					rotate = 270;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					rotate = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_90:
					rotate = 90;
					break;
			}

			Log.i("RotateImage", "Exif orientation: " + orientation);
			Log.i("RotateImage", "Rotate value: " + rotate);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return rotate;
	}

}
