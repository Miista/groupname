import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * Created by Soren Palmund on 07-09-2015.
 */
public class CP
{
	public static class Tuple<X,Y> {
		final public X val1;
		final public Y val2;

		public Tuple(X x, Y y) {
			this.val1 = x;
			this.val2 = y;
		}
	}

	public static class EPoint
	{
		public final double x, y;

		public EPoint(double x, double y)
		{
			this.x = x;
			this.y = y;
		}

		public double distance(EPoint other)
		{
			final double y = Math.abs( this.y - other.y );
			final double x = Math.abs( this.x - other.x );
			return Math.sqrt( y * y + x * x );
		}
	}

	public static void main(String[] args) throws IOException
	{
		final List<File> collect = Files.walk( Paths.get( "cp-data" ) )
									.filter( Files::isRegularFile )
									.map( Path::toFile )
									.filter( f -> f.getName()
													.contains( "33810" ) )
									.collect( Collectors.toList() );

		Tuple<Double, List<EPoint>> result;
		ArrayList<EPoint> EPoints = null;
		for (File file : collect)
		{
			System.out.println(file);
			EPoints = CPParser.readPoints( file );
			Collections.sort( EPoints, (o1, o2) -> Double.compare( o1.x, o2.x ) );
			result = ClosestPair( EPoints );
			System.out.println(file + ": "+EPoints.size()+" "+result.val1);
			result = null;
		}
	}

	private static Tuple<Double, List<EPoint> > closestOf3( List<EPoint> list) {
		double d01 = list.get(0).distance( list.get(1) );
		double d02 = list.get(0).distance( list.get(2) );
		double delta = d01 < d02 ? d01 : d02;

		//Sort the elements
		if( list.get(0).y > list.get(1).y) Collections.swap(list, 0, 1); // compares 2 first elements , makes sure theyre in order
		if( list.get(1).y > list.get(2).y) Collections.swap(list, 1, 2); // compares 2 last elements, make sure they are in order
		if( list.get(0).y > list.get(1).y) Collections.swap(list, 0, 1); // compares 2 first again, in case order has been altered

		return new Tuple<Double, List<EPoint> >(delta, list);
	}

	public static Tuple<Double, List<EPoint> > ClosestPair( List<EPoint> input )
	{
		Double delta;
		switch (input.size())
		{
			case 3: // Only 3 EPoints - Base Case
				return closestOf3( input );
			case 2:
				if (input.get( 0 ).y > input.get( 1 ).y) { Collections.swap( input, 0, 1 ); }
				delta = input.get( 0 )
							 .distance( input.get( 1 ) );
				return new Tuple<Double, List<EPoint>>( delta, input );
			case 1: // This should not occur - only if the given list only has 1 point
				System.out.println( "Hit 1 element in queue" );
				System.exit( -2 );
				return null;
			default:
				/**
				 * We partition the input into 2 subsets.
				 */
				final int medianIndex = input.size() / 2;
				final Tuple<Double, List<EPoint>> Q = ClosestPair( input.subList( 0, medianIndex ) );
				final Tuple<Double, List<EPoint>> R = ClosestPair( input.subList( medianIndex, input.size() ) );

				/**
				 * We find the smallest delta based on the subsets
				 */
				delta = Math.min( Q.val1, R.val1 );

				/**
				 * Stitching the 2 presorted y-lists
				 */
				List<EPoint> recombine = new ArrayList<EPoint>( Q.val2.size() + R.val2.size() );
				ListIterator<EPoint> Qp = Q.val2.listIterator(), Rp = R.val2.listIterator();
				EPoint qp, rp;
				int qi = 0, ri = 0;
				for (int i = 0; i < input.size(); i++)
				{
					if (qi < Q.val2.size())
					{
						qp = Q.val2.get( qi );
						if (ri < R.val2.size())
						{
							rp = R.val2.get( ri );
							if (qp.y < rp.y)
							{
								recombine.add( qp );
								qi++;
							} else
							{
								recombine.add( rp );
								ri++;
							}
						} else
						{
							recombine.add( qp );
							qi++;
						}
					} else
					{
						if (ri < R.val2.size())
						{
							rp = R.val2.get( ri );
							recombine.add( rp );
							ri++;
						}
					}
				}

				final double medianX = input.get( medianIndex ).x;
				final int lowerXBound = (int) (medianX - delta);
				final int upperXBound = (int) (medianX + delta);

				for (int i = 0; i < recombine.size(); i++)
				{
					EPoint pt = recombine.get( i );
					if (pt.x <= lowerXBound || pt.x >= upperXBound)
					{
						continue;
					}
					Double dist;
					for (int j = i + 1; j < (i + 1) + 15; j++)
					{
						if (j == recombine.size()) { break; }
						dist = pt.distance( recombine.get( j ) );
						if (dist == 0.0)
						{
							continue;
						}
						delta = dist < delta ? dist : delta;
					}
				}

				return new Tuple<>( delta, recombine );
		}
	}

}
